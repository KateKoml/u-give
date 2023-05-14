package com.ugive.services.impl;

import com.ugive.dto.FavouriteDto;
import com.ugive.dto.PurchaseOfferDto;
import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.mappers.FavouriteMapper;
import com.ugive.mappers.PurchaseOfferMapper;
import com.ugive.models.Favourite;
import com.ugive.repositories.FavouriteRepository;
import com.ugive.repositories.PurchaseOfferRepository;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FavouriteServiceImpl implements FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final PurchaseOfferRepository purchaseOfferRepository;
    private final FavouriteMapper favouriteMapper;
    private final PurchaseOfferMapper offerMapper;

    @Override
    public Optional<Favourite> create(FavouriteDto favouriteDto) {
        Favourite favourite = favouriteMapper.toEntity(favouriteDto);
        return Optional.of(favouriteRepository.save(favourite));
    }

    @Override
    public Optional<Favourite> update(Long id, FavouriteDto favouriteDto) {
        Favourite favourite = favouriteCheck(id);
        if (Boolean.TRUE.equals(favourite.getIsDeleted())) {
            throw new ForbiddenChangeException("Offer is deleted from Favourites");
        }
        favouriteMapper.updateEntityFromDto(favouriteDto, favourite);
        return Optional.of(favouriteRepository.save(favourite));
    }

    @Override
    public List<FavouriteDto> findAll(int page, int size) {
        Page<Favourite> favouritePage = favouriteRepository.findAll(PageRequest.of(page, size, Sort.by("created").descending()));
        return favouritePage.getContent().stream()
                .map(favouriteMapper::toDto)
                .toList();
    }

    @Override
    public FavouriteDto findOne(Long id) {
        Favourite favourite = favouriteCheck(id);
        if (Boolean.TRUE.equals(favourite.getIsDeleted())) {
            throw new ForbiddenChangeException("Offer is deleted from Favourites");
        }
        return favouriteMapper.toDto(favourite);
    }

    @Override
    public void softDelete(Long id) {
        Favourite favourite = favouriteCheck(id);
        favourite.setIsDeleted(true);
        favourite.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        favouriteRepository.save(favourite);
    }

    @Scheduled(cron = "0 0 0 * * *") // "0 0 0 * * *" Запускать каждый день в полночь,  "0 */1 * * * *" каждая минута
    @Transactional
    public void deleteFromFavourite() {
        Timestamp expirationDate = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        favouriteRepository.deleteExpiredFavourite(expirationDate);
    }

    @Override
    public List<PurchaseOfferDto> getFavoritePurchaseOffersForUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());
        Page<Favourite> userFavorites = favouriteRepository.findAllByUserId(userId, pageable);

        return userFavorites.getContent().stream()
                .map(favorite -> offerMapper.toDto(purchaseOfferRepository.findById(favorite.getPurchaseOffer().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Purchase Offer not found with id " + favorite.getPurchaseOffer()))))
                .toList();
    }

    @Override
    public PurchaseOfferDto getPurchaseOfferByFavouriteId(Long userId, Long favouriteId) {
        Favourite favourite = favouriteRepository.findByIdAndUserId(favouriteId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Favourite not found with id " + favouriteId));
        return offerMapper.toDto(favourite.getPurchaseOffer());
    }

    private Favourite favouriteCheck(Long id) {
        return favouriteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This offer not in your Favourites."));
    }
}
