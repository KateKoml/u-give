package com.ugive.requests;

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
public class PurchaseOfferRequest {
    @NotNull
    private Long seller;
    private Long customer;
    @NotNull
    private Integer offerStatus;
    @Size(min = 3, max = 200)
    @NotNull
    private String productName;
    @NotNull
    private Integer productCategory;
    @NotNull
    private Integer productCondition;
    @NotNull
    private BigDecimal price;

}
