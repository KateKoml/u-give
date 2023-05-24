package com.ugive.services.impl;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.mappers.FavouriteMapper;
import com.ugive.models.Favourite;
import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.FavouriteRepository;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.requests.FavouriteRequest;
import com.ugive.services.FavouriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FavouriteServiceImpl implements FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final PurchaseOfferRepository purchaseOfferRepository;
    private final FavouriteMapper favouriteMapper;

    @Override
    @Transactional
    public Favourite create(FavouriteRequest favouriteRequest) {
        Favourite favourite = favouriteMapper.toEntity(favouriteRequest);
        return favouriteRepository.save(favourite);
    }

    @Override
    @Transactional
    public Favourite update(Long id, FavouriteRequest favouriteRequest) {
        Favourite favourite = favouriteCheck(id);
        if (favourite.isDeleted()) {
            throw new ForbiddenChangeException("Offer is deleted from Favourites");
        }
        favouriteMapper.updateEntityFromRequest(favouriteRequest, favourite);
        return favouriteRepository.save(favourite);
    }

    @Override
    public List<Favourite> findAll(int page, int size) {
        Page<Favourite> favouritePage = favouriteRepository.findAll(PageRequest.of(page, size, Sort.by("created").descending()));
        return favouritePage.getContent().stream()
                .filter(favourite -> !favourite.isDeleted())
                .toList();
    }

    @Override
    public Favourite findOne(Long id) {
        return favouriteCheck(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Favourite favourite = favouriteCheck(id);
        favouriteRepository.delete(favourite);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteFromFavourite() {
        Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        favouriteRepository.deleteExpiredFavourite(expirationDate);
    }

    @Override
    public List<PurchaseOffer> getFavouritePurchaseOffersForUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        Page<Favourite> userFavorites = favouriteRepository.findAllByUserId(userId, pageable);

        return userFavorites.getContent().stream()
                .map(favorite -> (purchaseOfferRepository.findById(favorite.getPurchaseOffer().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Purchase Offer not found with id " + favorite.getPurchaseOffer()))))
                .toList();
    }

    @Override
    public PurchaseOffer getPurchaseOfferByFavouriteId(Long favouriteId) {
        Favourite favourite = favouriteRepository.findById(favouriteId)
                .orElseThrow(() -> new EntityNotFoundException("Favourite " + favouriteId + " not found."));
        return favourite.getPurchaseOffer();
    }

    private Favourite favouriteCheck(Long id) {
        Favourite favourite = favouriteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This offer not in your Favourites."));
        if (favourite.isDeleted()) {
            throw new ForbiddenChangeException("Offer is deleted from Favourites");
        }
        return favourite;
    }
}
