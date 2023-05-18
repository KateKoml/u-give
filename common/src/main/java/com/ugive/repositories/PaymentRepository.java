package com.ugive.repositories;

import com.ugive.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends
        JpaRepository<Payment, Long>,
        PagingAndSortingRepository<Payment, Long>,
        CrudRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p JOIN p.offer o WHERE o.customer.id = :userId")
    public List<Payment> findByOfferCustomerId(@Param("userId") Long userId);
}