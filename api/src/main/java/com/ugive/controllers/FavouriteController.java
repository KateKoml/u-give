package com.ugive.controllers;

import com.ugive.dto.FavouriteRequest;
import com.ugive.models.Favourite;
import com.ugive.models.PurchaseOffer;
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
    public ResponseEntity<List<Favourite>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Favourite> favourites = favouriteService.findAll(page, size);
        return new ResponseEntity<>(favourites, HttpStatus.OK);
    }

    @GetMapping("/{userId}/favourites/{favouriteId}")
    public ResponseEntity<PurchaseOffer> getPurchaseOfferByFavouriteId(@PathVariable Long userId, @PathVariable Long favouriteId) {
        PurchaseOffer purchaseOffer = favouriteService.getPurchaseOfferByFavouriteId(userId, favouriteId);
        return new ResponseEntity<>(purchaseOffer, HttpStatus.OK);
    }

    @GetMapping("/{userId}/favourites")
    public ResponseEntity<List<PurchaseOffer>> getUserFavouritePurchaseOffers(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<PurchaseOffer> favouritePurchaseOffers = favouriteService.getFavouritePurchaseOffersForUser(userId, page, size);
        return new ResponseEntity<>(favouritePurchaseOffers, HttpStatus.OK);
    }

    @GetMapping("/favourites/{id}")
    public ResponseEntity<Favourite> getFavouriteById(@PathVariable Long id) {
        Favourite favourite = favouriteService.findOne(id);
        return new ResponseEntity<>(favourite, HttpStatus.OK);
    }

    @PostMapping("/favourites")
    public ResponseEntity<Optional<Favourite>> createFavourite(@Valid @RequestBody FavouriteRequest favouriteRequest) {
        Optional<Favourite> favourite = favouriteService.create(favouriteRequest);
        return new ResponseEntity<>(favourite, HttpStatus.CREATED);
    }

    @PutMapping("/favourites/{id}/update")
    public ResponseEntity<Optional<Favourite>> updateOffer(@PathVariable("id") Long id, @RequestBody FavouriteRequest favouriteRequest) {
        Optional<Favourite> favourite = favouriteService.update(id, favouriteRequest);
        return new ResponseEntity<>(favourite, HttpStatus.CREATED);
    }

    @DeleteMapping("/favourites/{id}")
    public ResponseEntity<String> deleteFavourite(@PathVariable("id") Long id) {
        favouriteService.softDelete(id);
        return new ResponseEntity<>("This offer is deleted from your Favourites.", HttpStatus.OK);
    }
}

