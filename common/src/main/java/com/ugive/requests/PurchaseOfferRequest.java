package com.ugive.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Schema(description = "Purchase Offer Request with all needed information")
public class PurchaseOfferRequest {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",
            type = "number", description = "Seller id")
    @NotNull
    private Long seller;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "46",
            type = "number", description = "Customer id")
    private Long customer;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",
            type = "number", description = "Offer status id")
    @NotNull
    private Integer offerStatus;

    @Size(min = 3, max = 200)
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Candle 260ml",
            type = "string", description = "Product name")
    @NotNull
    private String productName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "17",
            type = "number", description = "Product category id")
    @NotNull
    private Integer productCategory;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",
            type = "number", description = "Product condition id")
    @NotNull
    private Integer productCondition;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "30",
            type = "number", description = "Product price")
    @NotNull
    private BigDecimal price;

}
