package com.ugive.repositories;

import com.ugive.models.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface UserBalanceRepository extends
        JpaRepository<UserBalance, Long>,
        PagingAndSortingRepository<UserBalance, Long>,
        CrudRepository<UserBalance, Long> {
    UserBalance findByUserId(Long id);

    @Modifying
    @Query("DELETE FROM UserBalance ub WHERE ub.isDeleted = true AND ub.changed < :expirationDate")
    void deleteExpiredUserBalance(@Param("expirationDate") Timestamp expirationDate);
}