package com.ugive.services.impl;

import com.ugive.dto.PurchaseOfferDto;
import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.mappers.PurchaseOfferMapper;
import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.repositories.UserRepository;
import com.ugive.repositories.catalogs.OfferStatusRepository;
import com.ugive.services.PurchaseOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PurchaseOfferServiceImpl implements PurchaseOfferService {
    private final PurchaseOfferRepository offerRepository;
    private final UserRepository userRepository;
    private final OfferStatusRepository statusRepository;
    private final PurchaseOfferMapper purchaseOfferMapper;

    @Override
    public Optional<PurchaseOffer> create(PurchaseOfferDto offerDto) {
        PurchaseOffer offer = purchaseOfferMapper.toEntity(offerDto);
        return Optional.of(offerRepository.save(offer));
    }

    @Override
    public Optional<PurchaseOffer> update(Long id, PurchaseOfferDto offerDto) {
        PurchaseOffer offer = offerCheck(id);
        if (Boolean.TRUE.equals(offer.getIsDeleted())) {
            throw new ForbiddenChangeException("Offer is deleted");
        }
        purchaseOfferMapper.updateEntityFromDto(offerDto, offer);
        return Optional.of(offerRepository.save(offer));
    }

    @Override
    public List<PurchaseOfferDto> findAll(int page, int size) {
        Page<PurchaseOffer> offersPage = offerRepository.findAll(PageRequest.of(page, size, Sort.by("created")));
        return offersPage.getContent().stream()
                .map(purchaseOfferMapper::toDto)
                .toList();
    }

    public List<PurchaseOfferDto> findAll() {
        List<PurchaseOffer> offers = offerRepository.findAll();
        return offers.stream().map(purchaseOfferMapper::toDto).toList();
    }

    @Override
    public PurchaseOfferDto findOne(Long id) {
        PurchaseOffer offer = offerCheck(id);
        if (Boolean.TRUE.equals(offer.getIsDeleted())) {
            throw new ForbiddenChangeException("This offer is deleted");
        }
        return purchaseOfferMapper.toDto(offer);
    }

    @Override
    public void markAsSoldOffers(Long id, Long customerId) {
        PurchaseOffer offer = offerCheck(id);
        offer.setOfferStatus(statusRepository.findById(2).orElseThrow(() -> new EntityNotFoundException("Wrong status")));
        offer.setCustomer(userRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("User not found")));
        offer.setIsDeleted(true);
        offerRepository.save(offer);
    }

    @Override
    public void softDelete(Long id) {
        PurchaseOffer offer = offerCheck(id);
        offer.setIsDeleted(true);
        offer.setOfferStatus(statusRepository.findById(3).orElseThrow(() -> new EntityNotFoundException("This status doesn't exist")));
        offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        offerRepository.save(offer);
    }

    @Override
    public Optional<PurchaseOffer> resetOffer(Long id) {
        PurchaseOffer offer = offerCheck(id);
        if (Boolean.TRUE.equals(offer.getIsDeleted())) {
            offer.setIsDeleted(false);
            offer.setOfferStatus(statusRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("This status doesn't exist")));
            offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        }
        return Optional.of(offerRepository.save(offer));
    }

    @Scheduled(cron = "0 0 0 * * *") // Запускать каждый день в полночь,  "0 */1 * * * *" каждая минута
    @Transactional
    public void deleteExpiredUsers() {
        Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(5));
        offerRepository.deleteExpiredOffer(expirationDate);
    }

    private PurchaseOffer offerCheck(Long id) {
        return offerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This offer doesn't exist or was deleted."));
    }
}