package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.ProductCategory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

@Cacheable("c_product_categories")
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
}