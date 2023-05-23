package com.ugive.services;

import com.ugive.requests.PaymentRequest;
import com.ugive.models.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment create(PaymentRequest paymentRequest);

    Payment makePayment (Long purchaseOfferId, String type, Long customerId);

    Payment update(Long id, PaymentRequest paymentRequest);

    List<Payment> findAll(int page, int size);

    List<Payment> findAllForOneUser(Long userId);

    Payment findOne(Long id);

    void deleted(Long id);
}