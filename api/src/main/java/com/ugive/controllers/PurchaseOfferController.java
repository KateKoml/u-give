package com.ugive.controllers;

import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.PurchaseOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/offers")
@RequiredArgsConstructor
public class PurchaseOfferController {
    private final PurchaseOfferRepository purchaseOfferRepository;

    @GetMapping
    public ResponseEntity<Object> getAllOffers() {
        List<PurchaseOffer> purchaseOffers = purchaseOfferRepository.findAll();
        return new ResponseEntity<>(purchaseOffers, HttpStatus.OK);
    }

    @GetMapping("/search")
    public String searchByName(@RequestParam(name = "productName") String productName,
                               Model model) {
        List<PurchaseOffer> purchaseOffers = purchaseOfferRepository.findByProductNameContainingIgnoreCase(productName);
        model.addAttribute("purchaseOffers", purchaseOffers);
        return "offers-search-results";
    }
}
