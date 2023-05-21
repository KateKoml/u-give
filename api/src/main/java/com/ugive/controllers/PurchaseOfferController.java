package com.ugive.controllers;

import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.requests.PurchaseOfferRequest;
import com.ugive.services.PurchaseOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/rest/offers")
@RequiredArgsConstructor
public class PurchaseOfferController {
    private final PurchaseOfferRepository purchaseOfferRepository;
    private final PurchaseOfferService purchaseOfferService;

    @GetMapping
    public ResponseEntity<List<PurchaseOffer>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<PurchaseOffer> offers = purchaseOfferService.findAll(page, size);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOffer> getOfferById(@PathVariable Long id) {
        PurchaseOffer offer = purchaseOfferService.findOne(id);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<PurchaseOffer>> getOfferBySellerId(@PathVariable Long id) {
        List<PurchaseOffer> offers = purchaseOfferService.findByUserId(id);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PurchaseOffer> createOffer(@Valid @RequestBody PurchaseOfferRequest purchaseOfferRequest) {
        PurchaseOffer purchaseOffer = purchaseOfferService.create(purchaseOfferRequest);
        return new ResponseEntity<>(purchaseOffer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOffer> updateOffer(@PathVariable("id") Long id, @RequestBody PurchaseOfferRequest purchaseOfferRequest) {
        PurchaseOffer purchaseOffer = purchaseOfferService.update(id, purchaseOfferRequest);
        return new ResponseEntity<>(purchaseOffer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable("id") Long id) {
        purchaseOfferService.softDelete(id);
        return new ResponseEntity<>("This offer is deleted. You can restore it within 5 days.", HttpStatus.OK);
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<PurchaseOffer> restoreOffer(@PathVariable("id") Long id) {
        PurchaseOffer offer = purchaseOfferService.resetOffer(id);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PurchaseOffer>> searchByName(@RequestParam(name = "productName") String productName) {
        List<PurchaseOffer> purchaseOffers = purchaseOfferRepository.findByProductNameContainingIgnoreCase(productName);
        return new ResponseEntity<>(purchaseOffers, HttpStatus.OK);
    }

    @GetMapping("/search/parameters")
    public ResponseEntity<List<PurchaseOffer>> search(@RequestParam(name = "categoryName") String categoryName,
                                                      @RequestParam(name = "conditionName") String conditionName,
                                                      @RequestParam(name = "minPrice") BigDecimal minPrice,
                                                      @RequestParam(name = "maxPrice") BigDecimal maxPrice) {
        if (conditionName.equals("any")) {
            conditionName = "%";
        }
        List<PurchaseOffer> offers = purchaseOfferRepository.findByProductCategoryAndProductConditionAndPrice(categoryName, conditionName, minPrice, maxPrice);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
}
