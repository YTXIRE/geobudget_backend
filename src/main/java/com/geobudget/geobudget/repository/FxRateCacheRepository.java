package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.FxRateCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FxRateCacheRepository extends JpaRepository<FxRateCache, Long> {
    Optional<FxRateCache> findByRateDateAndFromCurrencyAndToCurrency(LocalDate rateDate, String fromCurrency, String toCurrency);
    
    List<FxRateCache> findByFromCurrencyAndToCurrencyAndRateDateBetweenOrderByRateDateAsc(
            String fromCurrency, String toCurrency, LocalDate fromDate, LocalDate toDate);
    
    List<FxRateCache> findByFromCurrencyAndToCurrencyOrderByRateDateDesc(String fromCurrency, String toCurrency);
}
