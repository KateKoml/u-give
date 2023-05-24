package com.ugive.services.impl;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.exceptions.MoneyTransactionException;
import com.ugive.mappers.PaymentMapper;
import com.ugive.models.Payment;
import com.ugive.models.PurchaseOffer;
import com.ugive.models.catalogs.PaymentType;
import com.ugive.repositories.PaymentRepository;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.repositories.catalogs.PaymentTypeRepository;
import com.ugive.requests.PaymentRequest;
import com.ugive.services.PaymentService;
import com.ugive.services.PurchaseOfferService;
import com.ugive.services.UserBalanceService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = Logger.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;
    private final PurchaseOfferRepository offerRepository;
    private final PurchaseOfferService offerService;
    private final PaymentTypeRepository typeRepository;
    private final UserBalanceService userBalanceService;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public Payment create(PaymentRequest paymentRequest) {
        Payment payment;
        try {
            payment = paymentMapper.toEntity(paymentRequest);
        } catch (ForbiddenChangeException e) {
            logger.error("Wrong mapping for entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment update(Long id, PaymentRequest paymentRequest) {
        Payment payment = paymentCheck(id);
        try {
            paymentMapper.updateEntityFromRequest(paymentRequest, payment);
        } catch (ForbiddenChangeException e) {
            logger.error("Error updating payment request to entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> findAll(int page, int size) {
        Page<Payment> paymentsPage = paymentRepository.findAll(PageRequest.of(page, size, Sort.by("created").descending()));
        return paymentsPage.getContent().stream()
                .filter(payment -> !payment.isDeleted())
                .toList();
    }

    @Override
    public List<Payment> findAllForOneUser(Long userId) {
        return paymentRepository.findPaymentsByCustomerId(userId);
    }

    @Override
    public Payment findOne(Long id) {
        return paymentCheck(id);
    }

    @Override
    @Transactional
    public Payment makePayment(Long purchaseOfferId, String type, Long customerId) {
        PurchaseOffer offer = offerRepository.findById(purchaseOfferId).orElseThrow(() -> new EntityNotFoundException("Offer not found"));
        PaymentType paymentType = typeRepository.findByType(type).orElseThrow(() -> new EntityNotFoundException("This type not available"));
        PaymentRequest paymentRequest = new PaymentRequest(purchaseOfferId, paymentType.getId());

        try {
            // 3 = digital wallet
            if (paymentType.getId() == 3) {
                userBalanceService.sendMoney(customerId, offer.getSeller().getId(), offer.getPrice());
                offerService.markAsSoldOffers(purchaseOfferId, customerId);
            }
        } catch (MoneyTransactionException e) {
            logger.error("Money  transaction was not successful");
            throw new MoneyTransactionException(e.getMessage());
        }

        return create(paymentRequest);
    }

    @Override
    @Transactional
    public void deleted(Long id) {
        Payment payment = paymentCheck(id);
        paymentRepository.delete(payment);
    }

    private Payment paymentCheck(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This payment doesn't exist or was deleted."));
        if (payment.isDeleted()) {
            logger.error("Payment is deleted (isDeleted = true)");
            throw new EntityNotFoundException("This message was deleted.");
        }
        return payment;
    }
}