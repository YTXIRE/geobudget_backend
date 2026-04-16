package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;
import java.util.List;

public record IncomeComparisonResponse(
    YearData currentYear,
    YearData previousYear,
    Comparison comparison
) {
    public record YearData(
        Integer year,
        BigDecimal totalIncome,
        BigDecimal monthlyAverage,
        TopMonth topMonth,
        List<BigDecimal> incomeByMonth
    ) {
        public record TopMonth(
            Integer month,
            BigDecimal amount
        ) {}
    }

    public record Comparison(
        BigDecimal absoluteChange,
        BigDecimal percentageChange,
        BigDecimal avgMonthlyChange,
        BigDecimal bestMonthChange,
        Boolean growthConsistent
    ) {}
}
