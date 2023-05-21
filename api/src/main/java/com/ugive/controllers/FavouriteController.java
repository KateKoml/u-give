package com.ugive.controllers;

import com.ugive.models.Favourite;
import com.ugive.models.PurchaseOffer;
import com.ugive.requests.FavouriteRequest;
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

@RestController
@RequestMapping("/rest/favourites")
@RequiredArgsConstructor
public class FavouriteController {
    private final FavouriteService favouriteService;

    @GetMapping
    public ResponseEntity<List<Favourite>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Favourite> favourites = favouriteService.findAll(page, size);
        return new ResponseEntity<>(favourites, HttpStatus.OK);
    }

    @GetMapping("/{favouriteId}/offers")
    public ResponseEntity<PurchaseOffer> getPurchaseOfferByFavouriteId(@PathVariable Long favouriteId) {
        PurchaseOffer purchaseOffer = favouriteService.getPurchaseOfferByFavouriteId(favouriteId);
        return new ResponseEntity<>(purchaseOffer, HttpStatus.OK);
    }

    @GetMapping("/offers/users/{userId}")
    public ResponseEntity<List<PurchaseOffer>> getUserFavouritePurchaseOffers(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<PurchaseOffer> favouritePurchaseOffers = favouriteService.getFavouritePurchaseOffersForUser(userId, page, size);
        return new ResponseEntity<>(favouritePurchaseOffers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Favourite> getFavouriteById(@PathVariable Long id) {
        Favourite favourite = favouriteService.findOne(id);
        return new ResponseEntity<>(favourite, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Favourite> createFavourite(@Valid @RequestBody FavouriteRequest favouriteRequest) {
        Favourite favourite = favouriteService.create(favouriteRequest);
        return new ResponseEntity<>(favourite, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Favourite> updateOffer(@PathVariable("id") Long id, @RequestBody FavouriteRequest favouriteRequest) {
        Favourite favourite = favouriteService.update(id, favouriteRequest);
        return new ResponseEntity<>(favourite, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFavourite(@PathVariable("id") Long id) {
        favouriteService.delete(id);
        return new ResponseEntity<>("This offer is deleted from your Favourites.", HttpStatus.OK);
    }
}

