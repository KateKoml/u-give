package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PaymentTypeRepository extends
        JpaRepository<PaymentType, Integer>,
        PagingAndSortingRepository<PaymentType, Integer>,
        CrudRepository<PaymentType, Integer> {
}