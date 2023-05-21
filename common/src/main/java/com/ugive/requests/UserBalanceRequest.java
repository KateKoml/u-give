package com.ugive.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "User Balance Request with fields person and money")
public class UserBalanceRequest {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",
            type = "number", description = "User id")
    @NotNull
    private Long person;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "0.00",
            type = "number", description = "Money on balance")
    @NotNull
    private BigDecimal money;
}
