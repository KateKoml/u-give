package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.ProductCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductConditionRepository extends
        JpaRepository<ProductCondition, Integer>,
        PagingAndSortingRepository<ProductCondition, Integer>,
        CrudRepository<ProductCondition, Integer> {
}