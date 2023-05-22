package com.ugive.controllers;

import com.ugive.exceptions.ValidationCheckException;
import com.ugive.models.PurchaseOffer;
import com.ugive.repositories.PurchaseOfferRepository;
import com.ugive.requests.PurchaseOfferRequest;
import com.ugive.services.EmailSenderService;
import com.ugive.services.PurchaseOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/rest/offers")
@RequiredArgsConstructor
public class PurchaseOfferController {
    private static final Logger logger = Logger.getLogger(PurchaseOfferController.class);
    private final PurchaseOfferRepository purchaseOfferRepository;
    private final EmailSenderService emailSenderService;
    private final PurchaseOfferService purchaseOfferService;

    @Operation(
            summary = "Spring Data Purchase Offer Search with Pageable Params",
            description = "Load page with Pageable Params page and size sorting by \"created\"",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded Offers",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PageImpl.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Offers not found"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PurchaseOffer>> findAllByPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        List<PurchaseOffer> offers = purchaseOfferService.findAll(page, size);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Purchase Offers Search by Offer Id",
            description = "Load offer by Id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded Offer",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PurchaseOffer.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Offer not found"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOffer> getOfferById(@PathVariable Long id) {
        PurchaseOffer offer = purchaseOfferService.findOne(id);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Purchase Offers Search by Seller Id",
            description = "Load offer by seller (user) Id",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded Offer",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PurchaseOffer.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Offer not found"
                    )
            }
    )
    @GetMapping("/users/{id}")
    public ResponseEntity<List<PurchaseOffer>> getOfferBySellerId(@PathVariable Long id) {
        List<PurchaseOffer> offers = purchaseOfferService.findByUserId(id);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Purchase Offers Create",
            description = "Create offer with request body",
            responses = {
                    @ApiResponse(
                            responseCode = "CREATED",
                            description = "Successfully created Offer",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PurchaseOffer.class)))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PurchaseOffer> createOffer(@Valid @RequestBody PurchaseOfferRequest purchaseOfferRequest,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationCheckException(errorMessage);
        }

        PurchaseOffer purchaseOffer = purchaseOfferService.create(purchaseOfferRequest);

        String emailMessage = "Your offer \"" + purchaseOffer.getProductName() + "\"  with price "
                + purchaseOffer.getPrice() + " BYN was added on our website. \nWe hope You will find your customer! " +
                "Good luck and have a nice day! \n\n\nWith all wishes, U-Give ^_^";
        try {
            emailSenderService.sendEmail(purchaseOffer.getSeller().getAuthenticationInfo().getEmail(),
                    "We glad that You decided to use our application! ", emailMessage);
        } catch (MailException exception) {
            logger.error("Error sending message");
        }
        return new ResponseEntity<>(purchaseOffer, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Spring Data Purchase Offers Update",
            description = "Update offer by Id with request body",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully updated Offer",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PurchaseOffer.class)))
                    ),
                    @ApiResponse(
                            responseCode = "BAD_REQUEST",
                            description = "Validation error"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOffer> updateOffer(@PathVariable("id") Long id,
                                                     @Valid @RequestBody PurchaseOfferRequest purchaseOfferRequest,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            throw new ValidationCheckException(errorMessage);
        }

        PurchaseOffer purchaseOffer = purchaseOfferService.update(id, purchaseOfferRequest);
        return new ResponseEntity<>(purchaseOffer, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Delete Purchase Offer",
            description = "Delete Purchase Offer with possibility to restore within 5 days",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully deleted Offer",
                            content = @Content(mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Users not found"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable("id") Long id) {
        purchaseOfferService.softDelete(id);
        return new ResponseEntity<>("This offer is deleted. You can restore it within 5 days.", HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Restore Purchase Offer",
            description = "Restore Purchase Offer if offer have this possibility",
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully restored Offer",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PurchaseOffer.class)))
                    ),
                    @ApiResponse(
                            responseCode = "NOT_FOUND",
                            description = "Users not found"
                    )
            }
    )
    @PutMapping("/{id}/restore")
    public ResponseEntity<PurchaseOffer> restoreOffer(@PathVariable("id") Long id) {
        PurchaseOffer offer = purchaseOfferService.resetOffer(id);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Purchase Offers Search by Name",
            description = "Search offers by name",
            parameters = {
                    @Parameter(name = "productName",
                            in = ParameterIn.QUERY,
                            schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
                                    example = "Candle", type = "string", description = "product name"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded offers",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PurchaseOffer.class)))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error")
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<PurchaseOffer>> searchByName(@RequestParam(name = "productName") String productName) {
        List<PurchaseOffer> purchaseOffers = purchaseOfferRepository.findByProductNameContainingIgnoreCase(productName);
        return new ResponseEntity<>(purchaseOffers, HttpStatus.OK);
    }

    @Operation(
            summary = "Spring Data Purchase Offers Search by given params",
            description = "Search offers by category name, condition name, min and max price",
            parameters = {
                    @Parameter(name = "categoryName",
                            in = ParameterIn.QUERY,
                            schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
                                    example = "hobbies", type = "string", description = "product category")),
                    @Parameter(name = "conditionName",
                            in = ParameterIn.QUERY,
                            schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
                                    example = "new", type = "string", description = "product condition")),
                    @Parameter(name = "minPrice",
                            in = ParameterIn.QUERY,
                            schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
                                    example = "0", type = "number", description = "max price for search")),
                    @Parameter(name = "maxPrice",
                            in = ParameterIn.QUERY,
                            schema = @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
                                    example = "100", type = "number", description = "user phone"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "OK",
                            description = "Successfully loaded offers",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PurchaseOffer.class)))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error")
            }
    )
    @GetMapping("/search/parameters")
    public ResponseEntity<List<PurchaseOffer>> search(@RequestParam(name = "categoryName") String categoryName,
                                                      @RequestParam(name = "conditionName") String conditionName,
                                                      @RequestParam(name = "minPrice") BigDecimal minPrice,
                                                      @RequestParam(name = "maxPrice") BigDecimal maxPrice) {
        if (conditionName.equals("any")) {
            conditionName = "%";
        }
        List<PurchaseOffer> offers = purchaseOfferRepository.findByProductCategoryAndProductConditionAndPrice(categoryName, conditionName, minPrice, maxPrice);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
}
