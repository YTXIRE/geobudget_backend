package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeSummaryResponse(
    PeriodInfo period,
    BigDecimal totalIncome,
    BigDecimal totalExpense,
    BigDecimal netBalance,
    BigDecimal savingsRate,
    BigDecimal incomeGrowth,
    java.util.List<CategoryBreakdown> topCategories,
    java.util.List<PartnerBreakdown> topPartners,
    BigDecimal dailyAverage,
    Long transactionCount
) {
    public record PeriodInfo(
        String type,
        LocalDate start,
        LocalDate end
    ) {}

    public record CategoryBreakdown(
        String category,
        BigDecimal amount,
        BigDecimal percentage
    ) {}

    public record PartnerBreakdown(
        String partner,
        BigDecimal amount
    ) {}
}
