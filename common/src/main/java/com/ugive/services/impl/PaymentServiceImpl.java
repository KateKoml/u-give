package com.ugive.services.impl;

import com.ugive.dto.PaymentDto;
import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.mappers.PaymentMapper;
import com.ugive.models.Payment;
import com.ugive.models.PurchaseOffer;
import com.ugive.models.catalogs.PaymentType;
import com.ugive.repositories.PaymentRepository;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.repositories.catalogs.OfferStatusRepository;
import com.ugive.repositories.catalogs.PaymentTypeRepository;
import com.ugive.services.PaymentService;
import com.ugive.services.PurchaseOfferService;
import com.ugive.services.UserBalanceService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PurchaseOfferRepository offerRepository;
    private final OfferStatusRepository statusRepository;
    private final PurchaseOfferService offerService;
    private final PaymentTypeRepository typeRepository;
    private final UserBalanceService userBalanceService;
    private final PaymentMapper paymentMapper;

    @Override
    public Optional<Payment> create(PaymentDto paymentDto) {
        Payment payment = paymentMapper.toEntity(paymentDto);
        return Optional.of(paymentRepository.save(payment));
    }

    @Override
    public Optional<Payment> update(Long id, PaymentDto paymentDto) {
        Payment payment = paymentCheck(id);
        paymentMapper.updateEntityFromDto(paymentDto, payment);
        return Optional.of(paymentRepository.save(payment));
    }

    @Override
    public List<PaymentDto> findAll(int page, int size) {
        Page<Payment> paymentsPage = paymentRepository.findAll(PageRequest.of(page, size, Sort.by("created").descending()));
        return paymentsPage.getContent().stream()
                .filter(payment -> !payment.getIsDeleted())
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public List<PaymentDto> findAllForOneUser(Long userId) {
        List<Payment> payments = paymentRepository.findByOfferCustomerId(userId);
        return payments.stream()
                .filter(payment -> !payment.getIsDeleted())
                .sorted(Comparator.comparing(Payment::getCreated).reversed())
                .map(paymentMapper::toDto).toList();
    }

    @Override
    public PaymentDto findOne(Long id) {
        Payment payment = paymentCheck(id);
        return paymentMapper.toDto(payment);
    }

    @Override
    @Transactional
    public Optional<Payment> makePayment (Long purchaseOfferId, String type, Long customerId) {
        PurchaseOffer offer = offerRepository.findById(purchaseOfferId).orElseThrow(() -> new EntityNotFoundException("Offer not found"));
        PaymentType paymentType = typeRepository.findByType(type).orElseThrow(() -> new EntityNotFoundException("This type not available"));
        PaymentDto paymentDto = new PaymentDto(purchaseOfferId, paymentType.getId());

        if(paymentType.getId() == 3) {
            userBalanceService.sendMoney(customerId, offer.getSeller().getId(), offer.getPrice());
            offerService.markAsSoldOffers(purchaseOfferId, customerId);
        }

        return create(paymentDto);
    }

    @Override
    public void markAsDeleted(Long id) {
        Payment payment = paymentCheck(id);
        payment.setIsDeleted(true);
        payment.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        paymentRepository.save(payment);
    }

    private Payment paymentCheck(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This payment doesn't exist or was deleted."));
        if (Boolean.TRUE.equals(payment.getIsDeleted())) {
            throw new EntityNotFoundException("This message was deleted.");
        }
        return payment;
    }
}