package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;
import java.util.List;

public record IncomeByCategoryResponse(
    List<CategoryIncome> categories,
    BigDecimal totalIncome
) {
    public record CategoryIncome(
        Long categoryId,
        String categoryName,
        String icon,
        String color,
        BigDecimal amount,
        BigDecimal percentage,
        BigDecimal percentageChange
    ) {}
}
