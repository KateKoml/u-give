package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.ProductCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductConditionRepository extends JpaRepository<ProductCondition, Integer> {
}