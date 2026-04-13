package com.geobudget.geobudget.service;

import com.geobudget.geobudget.entity.FxRateCache;
import com.geobudget.geobudget.repository.FxRateCacheRepository;
import com.geobudget.geobudget.dto.fx.FxRateHistoryItem;
import com.geobudget.geobudget.dto.fx.FxRateResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
public class FxRateService {
    private static final String FX_URL = "https://api.frankfurter.dev/v2/rate/{from}/{to}?date={date}";
    private static final String PROVIDER = "frankfurter";
    private static final Logger log = LoggerFactory.getLogger(FxRateService.class);

    private final RestTemplate restTemplate;
    private final FxRateCacheRepository fxRateCacheRepository;

    public FxRateService(RestTemplate restTemplate, FxRateCacheRepository fxRateCacheRepository) {
        this.restTemplate = restTemplate;
        this.fxRateCacheRepository = fxRateCacheRepository;
    }

    public FxRateResponse getRate(String fromCurrency, String toCurrency, LocalDate rateDate) {
        String from = normalizeCurrency(fromCurrency);
        String to = normalizeCurrency(toCurrency);
        LocalDate effectiveDate = rateDate != null ? rateDate : LocalDate.now();

        if (from.equals(to)) {
            return new FxRateResponse(
                    from,
                    to,
                    effectiveDate,
                    BigDecimal.ONE.setScale(6, RoundingMode.HALF_UP),
                    PROVIDER,
                    true
            );
        }

        FxRateCache cached = fxRateCacheRepository
                .findByRateDateAndFromCurrencyAndToCurrency(effectiveDate, from, to)
                .orElse(null);
        if (cached != null) {
            return new FxRateResponse(
                    from,
                    to,
                    effectiveDate,
                    cached.getRate(),
                    cached.getProvider(),
                    true
            );
        }

        try {
            String response = restTemplate.getForObject(FX_URL, String.class, from, to, effectiveDate);
            if (response == null || response.isBlank()) {
                throw new IllegalStateException("Empty FX response");
            }

            JSONObject json = new JSONObject(response);
            if (!json.has("rate")) {
                throw new IllegalStateException("FX rate is missing in response");
            }

            BigDecimal rate = BigDecimal.valueOf(json.getDouble("rate"))
                    .setScale(6, RoundingMode.HALF_UP);

            // Check again in case another thread saved it while we were fetching
            FxRateCache existing = fxRateCacheRepository
                    .findByRateDateAndFromCurrencyAndToCurrency(effectiveDate, from, to)
                    .orElse(null);
            
            if (existing != null) {
                return new FxRateResponse(from, to, effectiveDate, existing.getRate(), existing.getProvider(), true);
            }

            FxRateCache cache = new FxRateCache();
            cache.setRateDate(effectiveDate);
            cache.setFromCurrency(from);
            cache.setToCurrency(to);
            cache.setRate(rate);
            cache.setProvider(PROVIDER);
            
            try {
                fxRateCacheRepository.saveAndFlush(cache);
            } catch (Exception saveEx) {
                // Ignore duplicate key - another thread saved it
                log.debug("Rate already cached: {} -> {} on {}", from, to, effectiveDate);
            }

            log.info("FxRateService.getRate: {} -> {} on {} = {}", from, to, effectiveDate, rate);
            return new FxRateResponse(from, to, effectiveDate, rate, PROVIDER, false);
        } catch (Exception e) {
            log.warn("FxRateService.getRate: failed to resolve {} -> {} on {}", from, to, effectiveDate, e);

            if (cached != null) {
                return new FxRateResponse(
                        from,
                        to,
                        effectiveDate,
                        cached.getRate(),
                        cached.getProvider(),
                        true
                );
            }

            throw new IllegalStateException(
                    "Не удалось получить курс валют для " + from + " -> " + to + " на дату " + effectiveDate,
                    e
            );
        }
    }

    public List<FxRateHistoryItem> getRateHistory(String fromCurrency, String toCurrency, LocalDate fromDate, LocalDate toDate) {
        String from = normalizeCurrency(fromCurrency);
        String to = normalizeCurrency(toCurrency);
        
        if (from.equals(to)) {
            return List.of(new FxRateHistoryItem(LocalDate.now(), BigDecimal.ONE.setScale(6, RoundingMode.HALF_UP)));
        }

        LocalDate effectiveFrom = fromDate != null ? fromDate : LocalDate.now().minusDays(30);
        LocalDate effectiveTo = toDate != null ? toDate : LocalDate.now();

        List<FxRateCache> rates = fxRateCacheRepository
                .findByFromCurrencyAndToCurrencyAndRateDateBetweenOrderByRateDateAsc(from, to, effectiveFrom, effectiveTo);

        return rates.stream()
                .map(rate -> new FxRateHistoryItem(rate.getRateDate(), rate.getRate()))
                .toList();
    }

    public void populatePopularRates(int days) {
        List<String> bases = List.of("USD", "EUR", "GBP");
        List<String> targets = List.of("RUB", "USD", "EUR");
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(Math.min(days, 30));

        int count = 0;
        int maxCount = 100;
        
        for (String from : bases) {
            for (String to : targets) {
                if (from.equals(to)) continue;
                if (count >= maxCount) break;
                
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    if (count >= maxCount) break;
                    try {
                        getRate(from, to, date);
                        count++;
                    } catch (Exception e) {
                        log.debug("Skipped rate for {} -> {} on {}: {}", from, to, date, e.getMessage());
                    }
                }
            }
        }
        log.info("Populated {} exchange rates", count);
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
