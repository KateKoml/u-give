package com.ugive.services;

import com.ugive.models.Favourite;
import com.ugive.models.PurchaseOffer;
import com.ugive.requests.FavouriteRequest;

import java.util.List;

public interface FavouriteService {
    Favourite create(FavouriteRequest favouriteRequest);

    Favourite update(Long id, FavouriteRequest favouriteRequest);

    List<Favourite> findAll(int page, int size);

    Favourite findOne(Long id);

    void delete(Long id);

    List<PurchaseOffer> getFavouritePurchaseOffersForUser(Long userId, int page, int size);

    PurchaseOffer getPurchaseOfferByFavouriteId(Long favouriteId);
}
