package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;

public record GeoCityCategoryAnalyticsResponse(
        Long categoryId,
        String categoryName,
        Long transactionCount,
        BigDecimal incomeTotal,
        BigDecimal expenseTotal,
        BigDecimal balance
) {
}
