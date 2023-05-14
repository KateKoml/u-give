package com.ugive.repositories;

import com.ugive.models.PurchaseOffer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface PurchaseOfferRepository extends
        JpaRepository<PurchaseOffer, Long>,
        PagingAndSortingRepository<PurchaseOffer, Long>,
        CrudRepository<PurchaseOffer, Long> {
    @Cacheable("purchase_offers")
    List<PurchaseOffer> findByProductNameContainingIgnoreCase(String productName);;

    List<PurchaseOffer> findBySellerId(Long sellerId);

    @Cacheable("purchase_offers")
    @Query("SELECT po FROM PurchaseOffer po JOIN po.productCategory pc JOIN po.productCondition pcn " +
            "WHERE po.isDeleted = false AND pc.categoryName LIKE %:categoryName% AND pcn.conditionName LIKE %:conditionName% " +
            "AND po.price BETWEEN :minPrice AND :maxPrice")
    List<PurchaseOffer> findByProductCategoryAndProductConditionAndPrice(@Param("categoryName") String categoryName,
                                                                         @Param("conditionName") String conditionName,
                                                                         @Param("minPrice") BigDecimal minPrice,
                                                                         @Param("maxPrice") BigDecimal maxPrice);

    @Modifying
    @Query("DELETE FROM PurchaseOffer po WHERE po.isDeleted = true AND po.changed < :expirationDate")
    void deleteExpiredOffer(@Param("expirationDate") Timestamp expirationDate);
}
