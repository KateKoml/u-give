package com.ugive.controllers;

import com.ugive.models.UserBalance;
import com.ugive.repositories.UserBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
public class UserBalanceController {
    private final UserBalanceRepository userBalanceRepository;

    @GetMapping
    public ResponseEntity<Object> getAllUsersBalances() {
        List<UserBalance> balances = userBalanceRepository.findAll();
        return new ResponseEntity<>(balances, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBalanceById(@PathVariable Long id) {
        UserBalance userBalance = userBalanceRepository.findByUserId(id);
        return  new ResponseEntity<>(userBalance, HttpStatus.OK);
    }
}
