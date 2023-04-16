package com.ugive.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_offers")
public class PurchaseOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_category_id", nullable = false)
    private Integer productCategoryId;

    @Column(name = "product_condition_id", nullable = false)
    private Integer productConditionId;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.valueOf(0.0);

    @Column(nullable = false)
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());

    @Column(nullable = false)
    private Timestamp changed = Timestamp.valueOf(LocalDateTime.now());

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
