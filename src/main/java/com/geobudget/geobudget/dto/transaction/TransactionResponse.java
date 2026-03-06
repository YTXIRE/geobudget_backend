package com.geobudget.geobudget.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private String type;
    private BigDecimal amount;
    private Long categoryId;
    private TransactionCategoryDto category;
    private String description;
    private LocalDateTime occurredAt;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String city;
    private String country;
    private String placeId;
    private String locationSource;
    private BigDecimal originalAmount;
    private String originalCurrency;
    private BigDecimal rateToBase;
    private BigDecimal baseAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
