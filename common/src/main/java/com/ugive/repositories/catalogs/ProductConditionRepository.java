package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.ProductCondition;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

@Cacheable("c_product_conditions")
public interface ProductConditionRepository extends JpaRepository<ProductCondition, Integer> {
}