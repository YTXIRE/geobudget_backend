package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.fx.FxRateResponse;
import com.geobudget.geobudget.entity.FxRateCache;
import com.geobudget.geobudget.repository.FxRateCacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FxRateServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private FxRateCacheRepository fxRateCacheRepository;

    @InjectMocks
    private FxRateService fxRateService;

    @Test
    void getRate_fetchesHistoricalRateAndCachesIt() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        when(fxRateCacheRepository.findByRateDateAndFromCurrencyAndToCurrency(date, "USD", "RUB"))
                .thenReturn(Optional.empty());
        when(restTemplate.getForObject(
                eq("https://api.frankfurter.dev/v2/rate/{from}/{to}?date={date}"),
                eq(String.class),
                eq("USD"),
                eq("RUB"),
                eq(date)
        )).thenReturn("{\"date\":\"2024-01-15\",\"base\":\"USD\",\"quote\":\"RUB\",\"rate\":88.12}");

        FxRateResponse response = fxRateService.getRate("USD", "RUB", date);

        assertEquals(new BigDecimal("88.120000"), response.rate());
        assertEquals(date, response.date());
        assertTrue(!response.cached());

        ArgumentCaptor<FxRateCache> captor = ArgumentCaptor.forClass(FxRateCache.class);
        verify(fxRateCacheRepository).save(captor.capture());
        assertEquals(date, captor.getValue().getRateDate());
        assertEquals("USD", captor.getValue().getFromCurrency());
        assertEquals("RUB", captor.getValue().getToCurrency());
    }

    @Test
    void getRate_whenRemoteFails_returnsCachedRate() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        FxRateCache cache = new FxRateCache();
        cache.setRateDate(date);
        cache.setFromCurrency("USD");
        cache.setToCurrency("RUB");
        cache.setRate(new BigDecimal("88.120000"));
        cache.setProvider("frankfurter");

        when(fxRateCacheRepository.findByRateDateAndFromCurrencyAndToCurrency(date, "USD", "RUB"))
                .thenReturn(Optional.of(cache));

        FxRateResponse response = fxRateService.getRate("USD", "RUB", date);

        assertEquals(new BigDecimal("88.120000"), response.rate());
        assertTrue(response.cached());
    }
}
