package com.ugive.services;

import com.ugive.dto.FavouriteDto;
import com.ugive.dto.PurchaseOfferDto;
import com.ugive.models.Favourite;

import java.util.List;
import java.util.Optional;

public interface FavouriteService {
    Optional<Favourite> create(FavouriteDto favouriteDto);

    Optional<Favourite> update(Long id, FavouriteDto favouriteDto);

    List<FavouriteDto> findAll(int page, int size);

    public FavouriteDto findOne(Long id);

    void softDelete(Long id);

    List<PurchaseOfferDto> getFavoritePurchaseOffersForUser(Long userId, int page, int size);

    PurchaseOfferDto getPurchaseOfferByFavouriteId(Long userId, Long favouriteId);
}
