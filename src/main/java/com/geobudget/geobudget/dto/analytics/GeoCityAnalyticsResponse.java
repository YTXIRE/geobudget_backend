package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;

public record GeoCityAnalyticsResponse(
        String locationGroupKey,
        String locationLabel,
        String city,
        String country,
        String region,
        BigDecimal latitude,
        BigDecimal longitude,
        Long transactionCount,
        BigDecimal incomeTotal,
        BigDecimal expenseTotal,
        BigDecimal balance
) {
}
