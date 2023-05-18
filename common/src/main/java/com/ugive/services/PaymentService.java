package com.ugive.services;

import com.ugive.dto.PaymentRequest;
import com.ugive.models.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Optional<Payment> create(PaymentRequest paymentRequest);

    Optional<Payment> makePayment (Long purchaseOfferId, String type, Long customerId);

    Optional<Payment> update(Long id, PaymentRequest paymentRequest);

    List<Payment> findAll(int page, int size);

    List<Payment> findAllForOneUser(Long userId);

    Payment findOne(Long id);

    void markAsDeleted(Long id);
}