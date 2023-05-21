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
@Schema(description = "User Balance Request")
public class UserBalanceRequest {
    @NotNull
    private Long person;
    @NotNull
    private BigDecimal money;
}
