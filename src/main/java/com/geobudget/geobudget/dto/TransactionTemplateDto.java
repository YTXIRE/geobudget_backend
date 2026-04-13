package com.geobudget.geobudget.dto;

import java.math.BigDecimal;

public record TransactionTemplateDto(
    Long id,
    Long userId,
    String name,
    String type,
    Long categoryId,
    String categoryName,
    String categoryIcon,
    BigDecimal amount,
    String currency,
    String description,
    Boolean isActive,
    Boolean recurrenceEnabled,
    String recurrenceType,
    String recurrenceDays,
    Integer dayOfMonth,
    String city,
    String country,
    String region,
    Double latitude,
    Double longitude,
    String placeId,
    String locationSource
) {
}
