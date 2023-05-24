package com.ugive.services;

import com.ugive.models.Payment;
import com.ugive.requests.PaymentRequest;

import java.util.List;

public interface PaymentService {
    Payment create(PaymentRequest paymentRequest);

    Payment makePayment(Long purchaseOfferId, String type, Long customerId);

    Payment update(Long id, PaymentRequest paymentRequest);

    List<Payment> findAll(int page, int size);

    List<Payment> findAllForOneUser(Long userId);

    Payment findOne(Long id);

    void deleted(Long id);
}