package com.ugive.repositories;

import com.ugive.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentRepository extends
        JpaRepository<Payment, Long>,
        PagingAndSortingRepository<Payment, Long>,
        CrudRepository<Payment, Long> {

}