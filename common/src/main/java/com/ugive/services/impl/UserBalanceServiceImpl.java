package com.ugive.services.impl;

import com.ugive.dto.UserBalanceRequest;
import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.exceptions.MoneyTransactionException;
import com.ugive.mappers.UserBalanceMapper;
import com.ugive.models.UserBalance;
import com.ugive.repositories.UserBalanceRepository;
import com.ugive.services.UserBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserBalanceServiceImpl implements UserBalanceService {
    private final UserBalanceMapper userBalanceMapper;
    private final UserBalanceRepository userBalanceRepository;

    @Override
    public Optional<UserBalance> create(UserBalanceRequest userBalanceDto) {
        UserBalance userBalance = userBalanceMapper.toEntity(userBalanceDto);
        return Optional.of(userBalanceRepository.save(userBalance));
    }

    @Override
    public Optional<UserBalance> update(Long id, UserBalanceRequest userBalanceDto) {
        UserBalance userBalance = findOne(id);
        userBalanceMapper.updateEntityFromRequest(userBalanceDto, userBalance);
        return Optional.of(userBalanceRepository.save(userBalance));
    }

    @Override
    public List<UserBalance> findAll(int page, int size) {
        Page<UserBalance> userBalances = userBalanceRepository.findAll(PageRequest.of(page, size, Sort.by("id")));
        return userBalances.getContent().stream().toList();
    }

    @Override
    public UserBalance findOne(Long id) {
        UserBalance userBalance = userBalanceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This balance is not available."));
        if (userBalance.isDeleted()) {
            throw new ForbiddenChangeException("User balance is not available, user was deleted.");
        }
        return userBalance;
    }

    @Override
    public void softDelete(Long id) {
        UserBalance userBalance = findOne(id);
        userBalance.setDeleted(true);
        userBalance.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        userBalanceRepository.save(userBalance);
    }

    @Override
    @Transactional
    public UserBalance topUpBalance(Long id, BigDecimal money) {
        UserBalance userBalance = findOne(id);

        BigDecimal currentBalance = userBalance.getBalance();
        BigDecimal newBalance = currentBalance.add(money);

        userBalance.setBalance(newBalance.setScale(2, RoundingMode.UNNECESSARY));
        userBalance.setChanged(Timestamp.valueOf(LocalDateTime.now()));


        return userBalanceRepository.save(userBalance);
    }

    @Override
    @Transactional
    public UserBalance sendMoney(Long idFirstUser, Long idSecondUser, BigDecimal price) {
        UserBalance firstUserBalance = userBalanceRepository.findByUserId(idFirstUser);
        UserBalance secondUserBalance = userBalanceRepository.findByUserId(idSecondUser);
        BigDecimal currentBalance = firstUserBalance.getBalance();
        BigDecimal secondUser = secondUserBalance.getBalance();

        int compareMoney = currentBalance.compareTo(price);

        if (firstUserBalance.isDeleted()) {
            throw new ForbiddenChangeException("User balance " + idFirstUser + " is not available, user was deleted.");
        } else if (secondUserBalance.isDeleted()) {
            throw new ForbiddenChangeException("User balance " + idSecondUser + " is not available, user was deleted.");
        } else if (compareMoney <= 0) {
            throw new MoneyTransactionException("User with balance " + idFirstUser + " don't have enough money. Balance is " + currentBalance + ".");
        }

        BigDecimal newBalance = currentBalance.subtract(price);
        firstUserBalance.setBalance(newBalance.setScale(2, RoundingMode.UNNECESSARY));
        firstUserBalance.setChanged(Timestamp.valueOf(LocalDateTime.now()));

        BigDecimal secondUserNewBalance = secondUser.add(price);
        secondUserBalance.setBalance(secondUserNewBalance.setScale(2, RoundingMode.UNNECESSARY));
        secondUserBalance.setChanged(Timestamp.valueOf(LocalDateTime.now()));

        userBalanceRepository.save(firstUserBalance);
        userBalanceRepository.save(secondUserBalance);
        return firstUserBalance;
    }

    @Override
    public UserBalance getBalanceByUserId(Long userId) {
        UserBalance userBalance;
        try {
            userBalance = userBalanceRepository.findByUserId(userId);
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("User not found.");
        }
        return userBalance;
    }
}
