package com.ugive.controllers;

import com.ugive.dto.UserBalanceDto;
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
    public ResponseEntity<List<UserBalanceDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<UserBalanceDto> userBalances = userBalanceService.findAll(page, size);
        return new ResponseEntity<>(userBalances, HttpStatus.OK);
    }

    @GetMapping("/balance/{id}")
    public ResponseEntity<UserBalanceDto> getBalanceById(@PathVariable Long id) {
        return ResponseEntity.ok(userBalanceService.findOne(id));
    }

    @GetMapping("{userId}/balance")
    public ResponseEntity<UserBalanceDto> getBalanceByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userBalanceService.getBalanceByUserId(userId));
    }

    @PostMapping("/balance/create")
    public ResponseEntity<Optional<UserBalance>> createUserBalance(@Valid @RequestBody UserBalanceDto userBalanceDto) {
        Optional<UserBalance> userBalance = userBalanceService.create(userBalanceDto);
        return new ResponseEntity<>(userBalance, HttpStatus.CREATED);
    }

    @PutMapping("/balance/{id}/update")
    public ResponseEntity<Optional<UserBalance>> updateBalance(@PathVariable("id") Long id, @RequestBody UserBalanceDto userBalanceDto) {
        Optional<UserBalance> userBalance = userBalanceService.update(id, userBalanceDto);
        return new ResponseEntity<>(userBalance, HttpStatus.CREATED);
    }

    @PutMapping("/balance/{id}/deposit")
    public ResponseEntity<UserBalanceDto> putMoneyOnBalance(@PathVariable("id") Long id, @RequestParam BigDecimal newMoney) {
        UserBalanceDto userBalance = userBalanceService.topUpBalance(id, newMoney);
        return new ResponseEntity<>(userBalance, HttpStatus.CREATED);
    }

    @PutMapping("/balance/{id}/send")
    public ResponseEntity<UserBalanceDto> putMoneyOnBalance(@PathVariable("id") Long idFirstUser,
                                                            @RequestParam Long idSecondUser,
                                                            @RequestParam BigDecimal newMoney) {
        UserBalanceDto userBalance = userBalanceService.sendMoney(idFirstUser, idSecondUser, newMoney);
        return new ResponseEntity<>(userBalance, HttpStatus.CREATED);
    }

    @DeleteMapping("/balance/{id}/delete")
    public ResponseEntity<String> deleteBalance(@PathVariable("id") Long id) {
        userBalanceService.softDelete(id);
        return new ResponseEntity<>("This balance is deleted.", HttpStatus.OK);
    }
}
