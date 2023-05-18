package com.ugive.mappers;

import com.ugive.dto.PurchaseOfferRequest;
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

    public PurchaseOffer toEntity(PurchaseOfferRequest offerRequest) {
        PurchaseOffer offer = modelMapper.map(offerRequest, PurchaseOffer.class);
        offer.setSeller(userService.findOne(offerRequest.getSeller()));
        if (offerRequest.getCustomer() != null) {
            offer.setCustomer(userService.findOne(offerRequest.getCustomer()));
        } else {
            offer.setCustomer(null);
        }
        offer.setOfferStatus(statusRepository.findById(offerRequest.getOfferStatus())
                .orElseThrow(() -> new EntityNotFoundException("This status doesn't exist")));
        offer.setProductCategory(categoryRepository.findById(offerRequest.getProductCategory())
                .orElseThrow(() -> new EntityNotFoundException("This category doesn't exist, try another one")));
        offer.setProductCondition(conditionRepository.findById(offerRequest.getProductCondition())
                .orElseThrow(() -> new EntityNotFoundException("This condition doesn't exist. Try new, used or any")));
        return offer;
    }

    public void updateEntityFromRequest(PurchaseOfferRequest offerRequest, PurchaseOffer offer) {
        if (offerRequest.getSeller() != null) {
            offer.setSeller(userService.findOne(offerRequest.getSeller()));
        }
        if (offerRequest.getCustomer() != null) {
            offer.setCustomer(userService.findOne(offerRequest.getCustomer()));
        }
        if (offerRequest.getOfferStatus() != null) {
            offer.setOfferStatus(statusRepository.findById(offerRequest.getOfferStatus())
                    .orElseThrow(() -> new EntityNotFoundException("This status doesn't exist")));
        }
        if (offerRequest.getProductName() != null) {
            offer.setProductName(offerRequest.getProductName());
        }
        if (offerRequest.getProductCategory() != null) {
            offer.setProductCategory(categoryRepository.findById(offerRequest.getProductCategory())
                    .orElseThrow(() -> new EntityNotFoundException("This category doesn't exist, try another one")));
        }
        if (offerRequest.getProductCondition() != null) {
            offer.setProductCondition(conditionRepository.findById(offerRequest.getProductCondition())
                    .orElseThrow(() -> new EntityNotFoundException("This condition doesn't exist. Try new, used or any")));
        }
        if (offerRequest.getPrice() != null) {
            offer.setPrice(offerRequest.getPrice());
        }
        offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
    }
}
