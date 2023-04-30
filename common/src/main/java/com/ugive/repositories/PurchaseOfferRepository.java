package com.ugive.repositories;

import com.ugive.models.PurchaseOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PurchaseOfferRepository extends
        JpaRepository<PurchaseOffer, Long>,
        PagingAndSortingRepository<PurchaseOffer, Long>,
        CrudRepository<PurchaseOffer, Long> {
    PurchaseOffer findByProductNameStartingWith(String productName);
}
