package com.ugive.services;

import com.ugive.models.PurchaseOffer;
import com.ugive.requests.PurchaseOfferRequest;

import java.util.List;

public interface PurchaseOfferService {
    PurchaseOffer create(PurchaseOfferRequest offerRequest);

    PurchaseOffer update(Long id, PurchaseOfferRequest offerRequest);

    List<PurchaseOffer> findAll(int page, int size);

    List<PurchaseOffer> findByUserId(Long sellerId);

    List<PurchaseOffer> findAll();

    public PurchaseOffer findOne(Long id);

    void markAsSoldOffers(Long id, Long customerId);

    void softDelete(Long id);

    PurchaseOffer resetOffer(Long id);
}
