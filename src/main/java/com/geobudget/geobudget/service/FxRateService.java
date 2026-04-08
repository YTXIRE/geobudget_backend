package com.geobudget.geobudget.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

@Service
public class FxRateService {
    private static final String FX_URL = "https://api.frankfurter.dev/v2/rate/{from}/{to}";
    private static final Logger log = LoggerFactory.getLogger(FxRateService.class);

    private final RestTemplate restTemplate;

    public FxRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal getRate(String fromCurrency, String toCurrency) {
        String from = normalizeCurrency(fromCurrency);
        String to = normalizeCurrency(toCurrency);

        if (from.equals(to)) {
            return BigDecimal.ONE.setScale(6, RoundingMode.HALF_UP);
        }

        try {
            String response = restTemplate.getForObject(FX_URL, String.class, from, to);
            if (response == null || response.isBlank()) {
                throw new IllegalStateException("Empty FX response");
            }

            JSONObject json = new JSONObject(response);
            if (!json.has("rate")) {
                throw new IllegalStateException("FX rate is missing in response");
            }

            BigDecimal rate = BigDecimal.valueOf(json.getDouble("rate"))
                    .setScale(6, RoundingMode.HALF_UP);
            log.info("FxRateService.getRate: {} -> {} = {}", from, to, rate);
            return rate;
        } catch (Exception e) {
            log.warn("FxRateService.getRate: failed to resolve {} -> {}", from, to, e);
            throw new IllegalStateException("Не удалось получить курс валют для " + from + " -> " + to, e);
        }
    }

    public String normalizeCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency must not be blank");
        }

        String normalized = currency.trim().toUpperCase(Locale.ROOT);
        if (!normalized.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("currency must be ISO-4217 (3 uppercase letters)");
        }
        return normalized;
    }
}
