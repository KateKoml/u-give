package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
}