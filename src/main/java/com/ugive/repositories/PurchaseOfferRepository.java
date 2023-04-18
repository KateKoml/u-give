package com.ugive.repositories;

import com.ugive.models.PurchaseOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOfferRepository extends JpaRepository<PurchaseOffer, Long> {
    PurchaseOffer findByProductName(String productName);
}
