package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;
import java.util.List;

public record IncomeForecastResponse(
    List<ForecastMonth> forecast,
    String methodology,
    BigDecimal annualProjection,
    Integer basedOnMonths
) {
    public record ForecastMonth(
        String month,
        BigDecimal predicted,
        BigDecimal confidence
    ) {}
}
