package com.geobudget.geobudget.dto.fx;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FxRateHistoryItem(
        LocalDate date,
        BigDecimal rate
) {
}
