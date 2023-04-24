package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.ProductCategory;
import com.ugive.models.catalogs.ProductCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductCategoryRepository extends
        JpaRepository<ProductCategory, Integer>,
        PagingAndSortingRepository<ProductCategory, Integer>,
        CrudRepository<ProductCategory, Integer> {
}