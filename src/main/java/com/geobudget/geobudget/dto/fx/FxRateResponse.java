package com.geobudget.geobudget.dto.fx;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FxRateResponse(
        String from,
        String to,
        LocalDate date,
        BigDecimal rate,
        String provider,
        boolean cached
) {
}
