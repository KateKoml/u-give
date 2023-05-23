package com.ugive.services;

import com.ugive.requests.UserBalanceRequest;
import com.ugive.models.UserBalance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserBalanceService {
    UserBalance create(UserBalanceRequest userBalanceRequest);

    UserBalance update(Long id, UserBalanceRequest userBalanceRequest);

    List<UserBalance> findAll(int page, int size);

    UserBalance findOne(Long id);

    void delete(Long id);

    UserBalance topUpBalance(Long id, BigDecimal money);

    UserBalance sendMoney(Long idFirstUser, Long idSecondUser, BigDecimal price);

    UserBalance getBalanceByUserId(Long userId);
}
