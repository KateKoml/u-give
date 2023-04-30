package com.ugive.repositories;

import com.ugive.models.PurchaseOffer;
import com.ugive.models.catalogs.ProductCategory;
import com.ugive.models.catalogs.ProductCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigDecimal;
import java.util.List;

public interface PurchaseOfferRepository extends
        JpaRepository<PurchaseOffer, Long>,
        PagingAndSortingRepository<PurchaseOffer, Long>,
        CrudRepository<PurchaseOffer, Long> {
    PurchaseOffer findByProductNameStartingWith(String productName);
//    List<PurchaseOffer> findAllByProductCategoryAndProductConditionAndPrice(ProductCategory categoryName, ProductCondition condition, BigDecimal price);
}
