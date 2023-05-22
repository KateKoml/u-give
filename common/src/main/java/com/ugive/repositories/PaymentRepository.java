package com.ugive.repositories;

import com.ugive.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p JOIN p.offer o WHERE o.customer.id = :userId AND p.isDeleted = false ORDER BY p.created DESC")
    List<Payment> findPaymentsByCustomerId(@Param("userId") Long userId);
}