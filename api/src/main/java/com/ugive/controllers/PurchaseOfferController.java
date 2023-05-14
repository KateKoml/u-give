package com.ugive.controllers;

import com.ugive.dto.PurchaseOfferDto;
import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.services.PurchaseOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/offers")
@RequiredArgsConstructor
public class PurchaseOfferController {
    private final PurchaseOfferRepository purchaseOfferRepository;
    private final PurchaseOfferService purchaseOfferService;

    @GetMapping
    public ResponseEntity<List<PurchaseOfferDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<PurchaseOfferDto> offers = purchaseOfferService.findAll(page, size);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOfferDto> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseOfferService.findOne(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Optional<PurchaseOffer>> createOffer(@Valid @RequestBody PurchaseOfferDto purchaseOfferDto) {
        Optional<PurchaseOffer> purchaseOffer = purchaseOfferService.create(purchaseOfferDto);
        return new ResponseEntity<>(purchaseOffer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Optional<PurchaseOffer>> updateOffer(@PathVariable("id") Long id, @RequestBody PurchaseOfferDto purchaseOfferDto) {
        Optional<PurchaseOffer> purchaseOffer = purchaseOfferService.update(id, purchaseOfferDto);
        return new ResponseEntity<>(purchaseOffer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/set_as_sold")
    public ResponseEntity<String> soldOffer(@PathVariable("id") Long id, @RequestParam Long customerId) {
        purchaseOfferService.markAsSoldOffers(id, customerId);
        return new ResponseEntity<>("This offer was sold out", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteOffer(@PathVariable("id") Long id) {
        purchaseOfferService.softDelete(id);
        return new ResponseEntity<>("This offer is deleted. You can restore it within 5 days.", HttpStatus.OK);
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Optional<PurchaseOffer>> restoreOffer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(purchaseOfferService.resetOffer(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public String searchByName(@RequestParam(name = "productName") String productName,
                               Model model) {
        List<PurchaseOffer> purchaseOffers = purchaseOfferRepository.findByProductNameContainingIgnoreCase(productName);
        model.addAttribute("purchaseOffers", purchaseOffers);
        return "offers-search-results";
    }
}
