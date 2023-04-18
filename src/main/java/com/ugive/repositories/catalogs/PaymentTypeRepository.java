package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, Integer> {
}