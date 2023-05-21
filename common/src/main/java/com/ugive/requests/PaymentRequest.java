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
@Schema(description = "Payment Request with offer id and payment type id")
public class PaymentRequest {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "17",
            type = "number", description = "Purchase Offer id")
    @NotNull
    private Long offerToPay;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "3",
            type = "number", description = "Payment type id")
    @NotNull
    private Integer typeOfPayment;
}