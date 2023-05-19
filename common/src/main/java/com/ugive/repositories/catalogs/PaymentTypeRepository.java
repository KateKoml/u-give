package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.PaymentType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Cacheable("c_payment_type")
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Integer> {
    Optional<PaymentType> findByType(String type);
}