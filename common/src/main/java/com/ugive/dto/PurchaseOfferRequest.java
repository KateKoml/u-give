package com.ugive.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOfferRequest {
    private Long seller;
    private Long customer;
    private Integer offerStatus;
    private String productName;
    private Integer productCategory;
    private Integer productCondition;
    private BigDecimal price;

}
