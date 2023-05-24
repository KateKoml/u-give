package com.ugive.services.impl;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.mappers.PurchaseOfferMapper;
import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.repositories.UserRepository;
import com.ugive.repositories.catalogs.OfferStatusRepository;
import com.ugive.requests.PurchaseOfferRequest;
import com.ugive.services.PurchaseOfferService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PurchaseOfferServiceImpl implements PurchaseOfferService {
    private static final Logger logger = Logger.getLogger(PurchaseOfferServiceImpl.class);
    private final PurchaseOfferRepository offerRepository;
    private final UserRepository userRepository;
    private final OfferStatusRepository statusRepository;
    private final PurchaseOfferMapper purchaseOfferMapper;

    @Override
    @Transactional
    public PurchaseOffer create(PurchaseOfferRequest offerRequest) {
        PurchaseOffer offer;
        try {
            offer = purchaseOfferMapper.toEntity(offerRequest);
        } catch (ForbiddenChangeException e) {
            logger.error("Wrong mapping for entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        checkIdsNotEqual(offerRequest.getSeller(), offerRequest.getCustomer());
        return offerRepository.save(offer);
    }

    @Override
    @Transactional
    public PurchaseOffer update(Long id, PurchaseOfferRequest offerRequest) {
        PurchaseOffer offer = offerCheck(id);
        try {
            purchaseOfferMapper.updateEntityFromRequest(offerRequest, offer);
        } catch (ForbiddenChangeException e) {
            logger.error("Error updating purchase offer request to entity." + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        checkIdsNotEqual(offer.getSeller().getId(), offer.getCustomer().getId());
        return offerRepository.save(offer);
    }

    @Override
    public List<PurchaseOffer> findAll(int page, int size) {
        Page<PurchaseOffer> offersPage = offerRepository.findAll(PageRequest.of(page, size, Sort.by("created").descending()));
        return offersPage.getContent()
                .stream()
                .filter(offer -> !offer.isDeleted())
                .toList();
    }

    @Override
    public List<PurchaseOffer> findAll() {
        return offerRepository.findAllByIsDeletedFalseOrderByIdAsc();
    }

    @Override
    public List<PurchaseOffer> findByUserId(Long sellerId) {
        return offerRepository.findBySellerId(sellerId);
    }

    @Override
    public PurchaseOffer findOne(Long id) {
        return offerCheck(id);
    }

    @Override
    @Transactional
    public void markAsSoldOffers(Long id, Long customerId) {
        PurchaseOffer offer = offerCheck(id);
        offer.setOfferStatus(statusRepository.findById(2).orElseThrow(() -> new EntityNotFoundException("Wrong status")));
        checkIdsNotEqual(offer.getSeller().getId(), customerId);
        offer.setCustomer(userRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("User not found")));
        offer.setDeleted(true);
        offerRepository.save(offer);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        PurchaseOffer offer = offerCheck(id);
        offer.setDeleted(true);
        offer.setOfferStatus(statusRepository.findById(3).orElseThrow(() -> new EntityNotFoundException("This status doesn't exist")));
        offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        offerRepository.save(offer);
    }

    @Override
    public PurchaseOffer resetOffer(Long id) {
        PurchaseOffer offer = offerCheck(id);
        if (offer.isDeleted()) {
            offer.setDeleted(false);
            offer.setOfferStatus(statusRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("This status doesn't exist")));
            offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        }
        return offerRepository.save(offer);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteExpiredOffer() {
        try {
            Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(5));
            List<PurchaseOffer> expiredOffers = offerRepository.findExpiredOffers(expirationDate);
            for (PurchaseOffer offer : expiredOffers) {
                offerRepository.deleteConnectedFavourite(offer);
                offerRepository.deleteConnectedPayment(offer);
                offerRepository.delete(offer);
            }
        } catch (SchedulingException e) {
            logger.error("Scheduling doesn't work correctly. " + e.getMessage());
        }
    }

    private PurchaseOffer offerCheck(Long id) {
        PurchaseOffer offer = offerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This offer doesn't exist or was deleted."));
        if (offer.isDeleted()) {
            logger.error("Offer is deleted (isDeleted = true)");
            throw new ForbiddenChangeException("Offer is deleted");
        }
        return offer;
    }

    private void checkIdsNotEqual(Long sellerId, Long customerId) {
        if (sellerId.equals(customerId)) {
            throw new ForbiddenChangeException("Seller and customer IDs cannot be the same.");
        }
    }
}
