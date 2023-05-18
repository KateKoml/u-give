package com.ugive.services;

import com.ugive.requests.FavouriteRequest;
import com.ugive.models.Favourite;
import com.ugive.models.PurchaseOffer;

import java.util.List;
import java.util.Optional;

public interface FavouriteService {
    Optional<Favourite> create(FavouriteRequest favouriteRequest);

    Optional<Favourite> update(Long id, FavouriteRequest favouriteRequest);

    List<Favourite> findAll(int page, int size);

    Favourite findOne(Long id);

    void softDelete(Long id);

    List<PurchaseOffer> getFavouritePurchaseOffersForUser(Long userId, int page, int size);

    PurchaseOffer getPurchaseOfferByFavouriteId(Long userId, Long favouriteId);
}
