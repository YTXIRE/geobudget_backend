package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GeoCityTransactionAnalyticsResponse(
        Long id,
        String type,
        Long categoryId,
        String categoryName,
        String description,
        BigDecimal amount,
        BigDecimal baseAmount,
        LocalDateTime occurredAt,
        String locationSource
) {
}
