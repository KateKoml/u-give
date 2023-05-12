package com.ugive.mappers;

import com.ugive.dto.FavouriteDto;
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

    public Favourite toEntity(FavouriteDto favouriteDto) {
        Favourite favourite = modelMapper.map(favouriteDto, Favourite.class);
        favourite.setUser(userService.findOne(favouriteDto.getUser()));
        Optional<PurchaseOffer> offer = offerRepository.findById(favouriteDto.getPurchaseOffer());
        if (Boolean.TRUE.equals(offer.get().getIsDeleted())) {
            throw new EntityNotFoundException("This offer doesn't exist or was deleted");
        }
        favourite.setPurchaseOffer(offer.orElseThrow(() -> new EntityNotFoundException("This offer doesn't exist")));
        return favourite;
    }

    public FavouriteDto toDto(Favourite favourite) {
        FavouriteDto favouriteDto = new FavouriteDto();
        favouriteDto.setUser(favourite.getUser().getId());
        favouriteDto.setPurchaseOffer(favourite.getPurchaseOffer().getId());
        return favouriteDto;
    }

    public void updateEntityFromDto(FavouriteDto favouriteDto, Favourite favourite) {
        if (favouriteDto.getUser() != null) {
            favourite.setUser(userService.findOne(favouriteDto.getUser()));
        }
        if (favouriteDto.getPurchaseOffer() != null) {
            Optional<PurchaseOffer> offer = offerRepository.findById(favouriteDto.getPurchaseOffer());
            if (Boolean.TRUE.equals(offer.get().getIsDeleted())) {
                throw new EntityNotFoundException("This offer doesn't exist or was deleted");
            }
            favourite.setPurchaseOffer(offer.orElseThrow(() -> new EntityNotFoundException("This offer doesn't exist")));
        }
    }
}
