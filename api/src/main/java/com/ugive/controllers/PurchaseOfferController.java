package com.ugive.controllers;

import com.ugive.models.PurchaseOffer;
import com.ugive.models.User;
import com.ugive.repositories.PurchaseOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class PurchaseOfferController {
    private final PurchaseOfferRepository purchaseOfferRepository;

    @GetMapping
    public ResponseEntity<Object> getAllOffers() {
        List<PurchaseOffer> purchaseOffers = purchaseOfferRepository.findAll();
        return new ResponseEntity<>(purchaseOffers, HttpStatus.OK);
    }
}
