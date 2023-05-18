package com.ugive.services;

import com.ugive.requests.PurchaseOfferRequest;
import com.ugive.models.PurchaseOffer;

import java.util.List;
import java.util.Optional;

public interface PurchaseOfferService {
    Optional<PurchaseOffer> create(PurchaseOfferRequest offerRequest);

    Optional<PurchaseOffer> update(Long id, PurchaseOfferRequest offerRequest);

    List<PurchaseOffer> findAll(int page, int size);

    List<PurchaseOffer> findAll();

    public PurchaseOffer findOne(Long id);

    void markAsSoldOffers(Long id, Long customerId);

    void softDelete(Long id);

    Optional<PurchaseOffer> resetOffer(Long id);
}
