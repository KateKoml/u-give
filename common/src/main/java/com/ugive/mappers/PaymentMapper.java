package com.ugive.mappers;

import com.ugive.requests.PaymentRequest;
import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.models.Payment;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.repositories.catalogs.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class PaymentMapper {
    private final ModelMapper modelMapper;
    private final PurchaseOfferRepository offerRepository;
    private final PaymentTypeRepository typeRepository;

    public Payment toEntity(PaymentRequest paymentRequest) {
        Payment payment = modelMapper.map(paymentRequest, Payment.class);
        payment.setOffer(offerRepository.findById(paymentRequest.getOfferToPay()).orElseThrow(
                () -> new EntityNotFoundException("Offer not found.")));
        payment.setPaymentType(typeRepository.findById(paymentRequest.getTypeOfPayment()).orElseThrow(
                () -> new EntityNotFoundException("This type doesn't exist. You can pay by card, cash, digital wallet, mobile phone.")));
        return payment;
    }

    public void updateEntityFromRequest(PaymentRequest paymentRequest, Payment payment) {
        if (paymentRequest.getOfferToPay() != null) {
            payment.setOffer(offerRepository.findById(paymentRequest.getOfferToPay()).orElseThrow(
                    () -> new EntityNotFoundException("Offer not found.")));
        }
        if (paymentRequest.getTypeOfPayment() != null) {
            payment.setPaymentType(typeRepository.findById(paymentRequest.getTypeOfPayment()).orElseThrow(
                    () -> new EntityNotFoundException("This type doesn't exist. You can pay by card, cash, digital wallet, mobile phone.")));
        }
        payment.setChanged(Timestamp.valueOf(LocalDateTime.now()));

    }
}