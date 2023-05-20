package com.ugive.repositories;

import com.ugive.models.PurchaseOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface PurchaseOfferRepository extends JpaRepository<PurchaseOffer, Long> {
    List<PurchaseOffer> findByProductNameContainingIgnoreCase(String productName);

    List<PurchaseOffer> findBySellerId(Long sellerId);

    @Query("SELECT po FROM PurchaseOffer po WHERE po.isDeleted = true AND po.changed < :expirationDate")
    List<PurchaseOffer> findExpiredOffers(Timestamp expirationDate);

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

    @Modifying
    @Query("DELETE FROM Favourite f WHERE f.purchaseOffer = :offer")
    void deleteConnectedFavourite(@Param("offer") PurchaseOffer offer);

    @Modifying
    @Query("DELETE FROM Payment p WHERE p.offer = :offer")
    void deleteConnectedPayment(@Param("offer") PurchaseOffer offer);
}
