package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;
import java.util.List;

public record IncomeTrendsResponse(
    List<MonthlyData> trends,
    TrendsSummary summary,
    BigDecimal growthRate
) {
    public record MonthlyData(
        String month,
        BigDecimal income,
        BigDecimal expense,
        BigDecimal net
    ) {}

    public record TrendsSummary(
        BigDecimal avgMonthlyIncome,
        BigDecimal avgMonthlyExpense,
        BigDecimal avgMonthlyNet,
        BestWorstMonth bestMonth,
        BestWorstMonth worstMonth
    ) {
        public record BestWorstMonth(
            String month,
            BigDecimal net
        ) {}
    }
}
