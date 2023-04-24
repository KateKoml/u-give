package com.ugive.repositories;

import com.ugive.models.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserBalanceRepository extends
        JpaRepository<UserBalance, Long>,
        PagingAndSortingRepository<UserBalance, Long>,
        CrudRepository<UserBalance, Long> {

}