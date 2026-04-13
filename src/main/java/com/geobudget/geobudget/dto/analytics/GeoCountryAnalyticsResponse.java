package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;

public record GeoCountryAnalyticsResponse(
        String country,
        Long transactionCount,
        BigDecimal incomeTotal,
        BigDecimal expenseTotal,
        BigDecimal balance
) {
}
