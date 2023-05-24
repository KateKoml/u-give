package com.ugive.repositories;

import com.ugive.models.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
    UserBalance findByUserId(Long id);
    void deleteById(Long id);
}