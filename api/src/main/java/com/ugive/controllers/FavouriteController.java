package com.ugive.controllers;

import com.ugive.dto.FavouriteDto;
import com.ugive.dto.PurchaseOfferDto;
import com.ugive.models.Favourite;
import com.ugive.services.FavouriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FavouriteController {
    private final FavouriteService favouriteService;

    @GetMapping("/favourites")
    public ResponseEntity<List<FavouriteDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<FavouriteDto> favourites = favouriteService.findAll(page, size);
        return new ResponseEntity<>(favourites, HttpStatus.OK);
    }

    @GetMapping("/{userId}/favourites/{favouriteId}")
    public ResponseEntity<PurchaseOfferDto> getPurchaseOfferByFavouriteId(@PathVariable Long userId, @PathVariable Long favouriteId) {
        PurchaseOfferDto purchaseOfferDto = favouriteService.getPurchaseOfferByFavouriteId(userId, favouriteId);
        return ResponseEntity.ok(purchaseOfferDto);
    }

    @GetMapping("/{userId}/favourites")
    public ResponseEntity<List<PurchaseOfferDto>> getUserFavouritePurchaseOffers(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<PurchaseOfferDto> favouritePurchaseOffers = favouriteService.getFavoritePurchaseOffersForUser(userId, page, size);
        return ResponseEntity.ok(favouritePurchaseOffers);
    }

    @GetMapping("/favourites/{id}")
    public ResponseEntity<FavouriteDto> getFavouriteById(@PathVariable Long id) {
        return ResponseEntity.ok(favouriteService.findOne(id));
    }

    @PostMapping("/favourites/create")
    public ResponseEntity<Optional<Favourite>> createOffer(@Valid @RequestBody FavouriteDto favouriteDto) {
        Optional<Favourite> favourite = favouriteService.create(favouriteDto);
        return new ResponseEntity<>(favourite, HttpStatus.CREATED);
    }

    @PutMapping("/favourites/{id}/update")
    public ResponseEntity<Optional<Favourite>> updateOffer(@PathVariable("id") Long id, @RequestBody FavouriteDto favouriteDto) {
        Optional<Favourite> favourite = favouriteService.update(id, favouriteDto);
        return new ResponseEntity<>(favourite, HttpStatus.CREATED);
    }

    @DeleteMapping("/favourites/{id}/delete")
    public ResponseEntity<String> deleteFavourite(@PathVariable("id") Long id) {
        favouriteService.softDelete(id);
        return new ResponseEntity<>("This offer is deleted from your Favourites.", HttpStatus.OK);
    }
}

