package com.ugive.services;

import com.ugive.dto.PurchaseOfferDto;
import com.ugive.models.PurchaseOffer;

import java.util.List;
import java.util.Optional;

public interface PurchaseOfferService {
    Optional<PurchaseOffer> create(PurchaseOfferDto offerDto);

    Optional<PurchaseOffer> update(Long id, PurchaseOfferDto offerDto);

    List<PurchaseOfferDto> findAll(int page, int size);

    List<PurchaseOfferDto> findAll();

    public PurchaseOfferDto findOne(Long id);

    void softDelete(Long id);

    Optional<PurchaseOffer> resetOffer(Long id);
}
