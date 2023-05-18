package com.ugive.mappers;

import com.ugive.dto.FavouriteRequest;
import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.models.Favourite;
import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class FavouriteMapper {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PurchaseOfferRepository offerRepository;

    public Favourite toEntity(FavouriteRequest favouriteRequest) {
        Favourite favourite = modelMapper.map(favouriteRequest, Favourite.class);
        favourite.setUser(userService.findOne(favouriteRequest.getUser()));
        Optional<PurchaseOffer> offer = offerRepository.findById(favouriteRequest.getPurchaseOffer());
        if (offer.get().isDeleted()) {
            throw new EntityNotFoundException("This offer doesn't exist or was deleted");
        }
        favourite.setPurchaseOffer(offer.orElseThrow(() -> new EntityNotFoundException("This offer doesn't exist")));
        return favourite;
    }

    public void updateEntityFromRequest(FavouriteRequest favouriteRequest, Favourite favourite) {
        if (favouriteRequest.getUser() != null) {
            favourite.setUser(userService.findOne(favouriteRequest.getUser()));
        }
        if (favouriteRequest.getPurchaseOffer() != null) {
            Optional<PurchaseOffer> offer = offerRepository.findById(favouriteRequest.getPurchaseOffer());
            if (offer.get().isDeleted()) {
                throw new EntityNotFoundException("This offer doesn't exist or was deleted");
            }
            favourite.setPurchaseOffer(offer.orElseThrow(() -> new EntityNotFoundException("This offer doesn't exist")));
        }
    }
}
