package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;
import java.util.List;

public record GeoCityAnalyticsDetailResponse(
        String key,
        String label,
        String city,
        String country,
        String region,
        BigDecimal latitude,
        BigDecimal longitude,
        Long transactionCount,
        BigDecimal incomeTotal,
        BigDecimal expenseTotal,
        BigDecimal balance,
        List<GeoCityCategoryAnalyticsResponse> categories,
        List<GeoCityTransactionAnalyticsResponse> transactions
) {
}
