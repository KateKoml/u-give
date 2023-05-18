package com.ugive.controllers;

import com.ugive.requests.PaymentRequest;
import com.ugive.models.Payment;
import com.ugive.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
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
    public ResponseEntity<Optional<Payment>> createPayment(@Valid @RequestBody PaymentRequest paymentDto) {
        Optional<Payment> payment = paymentService.create(paymentDto);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @PostMapping("/users/{customerId}/make_payment")
    public ResponseEntity<Optional<Payment>> makePayment(@PathVariable Long customerId,
                                                         @RequestParam Long purchaseOfferId,
                                                         @RequestParam String type) {
        Optional<Payment> payment = paymentService.makePayment(purchaseOfferId, type, customerId);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @PutMapping("{id}/update")
    public ResponseEntity<Optional<Payment>> updatePayment(@PathVariable("id") Long id, @RequestBody PaymentRequest paymentDto) {
        Optional<Payment> payment = paymentService.update(id, paymentDto);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deletePayment(@PathVariable("id") Long id) {
        paymentService.markAsDeleted(id);
        return new ResponseEntity<>("Payment is deleted.", HttpStatus.OK);
    }
}
