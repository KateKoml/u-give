package com.ugive.services;

import com.ugive.dto.PaymentDto;
import com.ugive.models.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Optional<Payment> create(PaymentDto paymentDto);

    Optional<Payment> makePayment (Long purchaseOfferId, String type, Long customerId);

    Optional<Payment> update(Long id, PaymentDto paymentDto);

    List<PaymentDto> findAll(int page, int size);

    List<PaymentDto> findAllForOneUser(Long userId);

    PaymentDto findOne(Long id);

    void markAsDeleted(Long id);
}