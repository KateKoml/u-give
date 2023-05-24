package com.ugive.controllers;

import com.ugive.models.Payment;
import com.ugive.requests.PaymentRequest;
import com.ugive.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Payment> payments = paymentService.findAll(page, size);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.findOne(id);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Payment>> getAllUserPayments(@PathVariable Long userId) {
        List<Payment> payments = paymentService.findAllForOneUser(userId);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        Payment payment = paymentService.create(paymentRequest);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @PostMapping("/users/{customerId}/pay")
    public ResponseEntity<Payment> makePayment(@PathVariable Long customerId,
                                               @RequestParam Long purchaseOfferId,
                                               @RequestParam String type) {
        Payment payment = paymentService.makePayment(purchaseOfferId, type, customerId);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable("id") Long id, @RequestBody PaymentRequest paymentRequest) {
        Payment payment = paymentService.update(id, paymentRequest);
        return new ResponseEntity<>(payment, HttpStatus.OK);
    }
}
