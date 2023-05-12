package com.ugive.mappers;

import com.ugive.dto.PurchaseOfferDto;
import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.catalogs.OfferStatusRepository;
import com.ugive.repositories.catalogs.ProductCategoryRepository;
import com.ugive.repositories.catalogs.ProductConditionRepository;
import com.ugive.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class PurchaseOfferMapper {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final OfferStatusRepository statusRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductConditionRepository conditionRepository;

    public PurchaseOffer toEntity(PurchaseOfferDto offerDto) {
        PurchaseOffer offer = modelMapper.map(offerDto, PurchaseOffer.class);
        offer.setSeller(userService.findOne(offerDto.getSeller()));
        if (offerDto.getCustomer() != null) {
            offer.setCustomer(userService.findOne(offerDto.getCustomer()));
        } else {
            offer.setCustomer(null);
        }
        offer.setOfferStatus(statusRepository.findById(offerDto.getOfferStatus())
                .orElseThrow(() -> new EntityNotFoundException("This status doesn't exist")));
        offer.setProductCategory(categoryRepository.findById(offerDto.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("This category doesn't exist, try another one")));
        offer.setProductCondition(conditionRepository.findById(offerDto.getProductCondition())
                .orElseThrow(() -> new EntityNotFoundException("This condition doesn't exist. Try new, used or any")));
        return offer;
    }

    public PurchaseOfferDto toDto(PurchaseOffer offer) {
        PurchaseOfferDto offerDto = new PurchaseOfferDto();
        offerDto.setSeller(offer.getSeller().getId());
        if (offer.getCustomer() != null) {
            offerDto.setCustomer(offer.getCustomer().getId());
        } else {
            offerDto.setCustomer(null);
        }
        offerDto.setOfferStatus(offer.getOfferStatus().getId());
        offerDto.setProductName(offer.getProductName());
        offerDto.setProductCategory(offer.getProductCategory().getId());
        offerDto.setProductCondition(offer.getProductCondition().getId());
        offerDto.setPrice(offer.getPrice());
        return offerDto;
    }

    public void updateEntityFromDto(PurchaseOfferDto offerDto, PurchaseOffer offer) {
        if (offerDto.getSeller() != null) {
            offer.setSeller(userService.findOne(offerDto.getSeller()));
        }
        if (offerDto.getCustomer() != null) {
            offer.setCustomer(userService.findOne(offerDto.getCustomer()));
        }
        if (offerDto.getOfferStatus() != null) {
            offer.setOfferStatus(statusRepository.findById(offerDto.getOfferStatus())
                    .orElseThrow(() -> new EntityNotFoundException("This status doesn't exist")));
        }
        if (offerDto.getProductName() != null) {
            offer.setProductName(offerDto.getProductName());
        }
        if (offerDto.getProductCategory() != null) {
            offer.setProductCategory(categoryRepository.findById(offerDto.getProductCategory())
                    .orElseThrow(() -> new EntityNotFoundException("This category doesn't exist, try another one")));
        }
        if (offerDto.getProductCondition() != null) {
            offer.setProductCondition(conditionRepository.findById(offerDto.getProductCondition())
                    .orElseThrow(() -> new EntityNotFoundException("This condition doesn't exist. Try new, used or any")));
        }
        if (offerDto.getPrice() != null) {
            offer.setPrice(offerDto.getPrice());
        }
        offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
    }
}
