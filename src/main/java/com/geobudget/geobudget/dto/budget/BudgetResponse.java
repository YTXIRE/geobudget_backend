package com.geobudget.geobudget.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponse {
    private Long id;
    private String name;
    private String periodType;
    private BigDecimal amountLimit;
    private String baseCurrency;
    private String scopeType;
    private Long categoryId;
    private String categoryName;
    private Long groupId;
    private String groupName;
    private String region;
    private String city;
    private String country;
    private LocalDate startsAt;
    private LocalDate endsAt;
    private BigDecimal warningThreshold;
    private Boolean isActive;
    private Long partnerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
