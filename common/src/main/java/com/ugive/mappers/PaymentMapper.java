package com.ugive.mappers;

import com.ugive.dto.PaymentDto;
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

    public Payment toEntity(PaymentDto paymentDto) {
        Payment payment = modelMapper.map(paymentDto, Payment.class);
        payment.setOffer(offerRepository.findById(paymentDto.getOfferToPay()).orElseThrow(
                () -> new EntityNotFoundException("Offer not found.")));
        payment.setPaymentType(typeRepository.findById(paymentDto.getTypeOfPayment()).orElseThrow(
                () -> new EntityNotFoundException("This type doesn't exist. You can pay by card, cash, digital wallet, mobile phone.")));
        return payment;
    }

    public PaymentDto toDto(Payment payment) {
        Long offerId = payment.getOffer().getId();
        Integer typeId = payment.getPaymentType().getId();
        PaymentDto paymentDto = modelMapper.map(payment, PaymentDto.class);
        paymentDto.setOfferToPay(offerId);
        paymentDto.setTypeOfPayment(typeId);
        return paymentDto;
    }

    public void updateEntityFromDto(PaymentDto paymentDto, Payment payment) {
        if (paymentDto.getOfferToPay() != null) {
            payment.setOffer(offerRepository.findById(paymentDto.getOfferToPay()).orElseThrow(
                    () -> new EntityNotFoundException("Offer not found.")));
        }
        if (paymentDto.getTypeOfPayment() != null) {
            payment.setPaymentType(typeRepository.findById(paymentDto.getTypeOfPayment()).orElseThrow(
                    () -> new EntityNotFoundException("This type doesn't exist. You can pay by card, cash, digital wallet, mobile phone.")));
        }
        payment.setChanged(Timestamp.valueOf(LocalDateTime.now()));

    }
}