package com.geobudget.geobudget.dto.fx;

import java.math.BigDecimal;

public record FxRateResponse(
        String from,
        String to,
        BigDecimal rate,
        String provider
) {
}
