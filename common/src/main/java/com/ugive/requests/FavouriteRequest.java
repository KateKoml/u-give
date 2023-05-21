package com.ugive.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Schema(description = "Favourite Request with offer and user id")
public class FavouriteRequest {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",
            type = "number", description = "User id")
    @NotNull
    private Long user;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",
            type = "number", description = "Purchase Offer id")
    @NotNull
    private Long purchaseOffer;
}
