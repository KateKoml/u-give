package com.ugive.controllers;

import com.ugive.dto.UserBalanceRequest;
import com.ugive.models.UserBalance;
import com.ugive.repositories.UserBalanceRepository;
import com.ugive.services.UserBalanceService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserBalanceController {
    private final UserBalanceService userBalanceService;

    @GetMapping("/balance")
    public ResponseEntity<List<UserBalance>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<UserBalance> userBalances = userBalanceService.findAll(page, size);
        return new ResponseEntity<>(userBalances, HttpStatus.OK);
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<UserBalance> getBalanceById(@PathVariable Long id) {
        UserBalance userBalance = userBalanceService.findOne(id);
        return new ResponseEntity<>(userBalance, HttpStatus.OK);
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<UserBalance> getBalanceByUserId(@PathVariable Long userId) {
        UserBalance userBalance = userBalanceService.getBalanceByUserId(userId);
        return new ResponseEntity<>(userBalance, HttpStatus.OK);
    }

    @PostMapping("/balance")
    public ResponseEntity<Optional<UserBalance>> createUserBalance(@Valid @RequestBody UserBalanceRequest userBalanceRequest) {
        Optional<UserBalance> userBalance = userBalanceService.create(userBalanceRequest);
        return new ResponseEntity<>(userBalance, HttpStatus.CREATED);
    }

    @PutMapping("/balance/{id}/update")
    public ResponseEntity<Optional<UserBalance>> updateBalance(@PathVariable("id") Long id, @RequestBody UserBalanceRequest userBalanceRequest) {
        Optional<UserBalance> userBalance = userBalanceService.update(id, userBalanceRequest);
        return new ResponseEntity<>(userBalance, HttpStatus.CREATED);
    }

    @PutMapping("/balance/{id}/deposit")
    public ResponseEntity<UserBalance> putMoneyOnBalance(@PathVariable("id") Long id, @RequestParam BigDecimal newMoney) {
        UserBalance userBalance = userBalanceService.topUpBalance(id, newMoney);
        return new ResponseEntity<>(userBalance, HttpStatus.CREATED);
    }

    @PutMapping("/balance/{id}/send")
    public ResponseEntity<UserBalance> putMoneyOnBalance(@PathVariable("id") Long idFirstUser,
                                                            @RequestParam Long idSecondUser,
                                                            @RequestParam BigDecimal newMoney) {
        UserBalance userBalance = userBalanceService.sendMoney(idFirstUser, idSecondUser, newMoney);
        return new ResponseEntity<>(userBalance, HttpStatus.CREATED);
    }

    @DeleteMapping("/balance/{id}/delete")
    public ResponseEntity<String> deleteBalance(@PathVariable("id") Long id) {
        userBalanceService.softDelete(id);
        return new ResponseEntity<>("This balance is deleted.", HttpStatus.OK);
    }
}
