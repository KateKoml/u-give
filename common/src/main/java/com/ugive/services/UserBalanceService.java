package com.ugive.services;

import com.ugive.dto.UserBalanceDto;
import com.ugive.models.UserBalance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserBalanceService {
    Optional<UserBalance> create(UserBalanceDto userBalanceDto);

    Optional<UserBalance> update(Long id, UserBalanceDto userBalanceDto);

    List<UserBalanceDto> findAll(int page, int size);

    UserBalanceDto findOne(Long id);

    void softDelete(Long id);

    UserBalanceDto topUpBalance(Long id, BigDecimal money);

    UserBalanceDto sendMoney(Long idFirstUser, Long idSecondUser, BigDecimal price);

    UserBalanceDto getBalanceByUserId(Long userId);
}
