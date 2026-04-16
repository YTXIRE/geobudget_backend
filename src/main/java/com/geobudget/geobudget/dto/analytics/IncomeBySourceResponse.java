package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;
import java.util.List;

public record IncomeBySourceResponse(
    List<SourceIncome> sources,
    BigDecimal totalIncome
) {
    public record SourceIncome(
        String source,
        String icon,
        String color,
        BigDecimal amount,
        BigDecimal percentage
    ) {}
}
