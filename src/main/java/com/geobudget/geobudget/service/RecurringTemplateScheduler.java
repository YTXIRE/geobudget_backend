package com.geobudget.geobudget.service;

import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.entity.TransactionTemplate;
import com.geobudget.geobudget.repository.TransactionRepository;
import com.geobudget.geobudget.repository.TransactionTemplateRepository;
import com.geobudget.geobudget.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecurringTemplateScheduler {
    
    private final TransactionTemplateRepository templateRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final FxRateService fxRateService;

    @Transactional
    @Scheduled(cron = "0 5 0 * * *")
    public void processRecurringTemplates() {
        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();
        int dayOfMonth = today.getDayOfMonth();
        int lastDayOfMonth = today.lengthOfMonth();
        log.info("Processing recurring templates for day of week: {}, day of month: {}", dayOfWeek, dayOfMonth);

        List<TransactionTemplate> templates = templateRepository
                .findByIsActiveTrueAndRecurrenceEnabledTrue();

        for (TransactionTemplate template : templates) {
            if (shouldRunToday(template, dayOfWeek, dayOfMonth, lastDayOfMonth)) {
                try {
                    createTransactionFromTemplate(template);
                    log.info("Created recurring transaction from template: {}", template.getName());
                } catch (Exception e) {
                    log.error("Failed to create transaction from template {}: {}", 
                            template.getId(), e.getMessage());
                }
            }
        }
    }

    private boolean shouldRunToday(TransactionTemplate template, int dayOfWeek, int dayOfMonth, int lastDayOfMonth) {
        String recurrenceType = template.getRecurrenceType();
        
        if ("day_of_month".equals(recurrenceType)) {
            Integer templateDay = template.getDayOfMonth();
            if (templateDay == null) {
                return false;
            }
            int effectiveDay = templateDay;
            if (templateDay > lastDayOfMonth) {
                effectiveDay = lastDayOfMonth;
            }
            return dayOfMonth == effectiveDay;
        } else {
            return isDayOfWeekMatch(template.getRecurrenceDays(), dayOfWeek);
        }
    }

    private boolean isDayOfWeekMatch(String recurrenceDays, int dayOfWeek) {
        if (recurrenceDays == null || recurrenceDays.isEmpty()) {
            return false;
        }
        String[] days = recurrenceDays.split(",");
        for (String day : days) {
            try {
                if (Integer.parseInt(day.trim()) == dayOfWeek) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    private void createTransactionFromTemplate(TransactionTemplate template) {
        Transaction transaction = new Transaction();
        transaction.setUserId(template.getUserId());
        transaction.setType(template.getType());
        transaction.setCategory(template.getCategory());
        transaction.setDescription(template.getDescription());
        transaction.setOccurredAt(LocalDateTime.now());
        transaction.setCity(template.getCity());
        transaction.setCountry(template.getCountry());
        transaction.setRegion(template.getRegion());
        transaction.setLatitude(template.getLatitude() != null 
                ? BigDecimal.valueOf(template.getLatitude()) : null);
        transaction.setLongitude(template.getLongitude() != null 
                ? BigDecimal.valueOf(template.getLongitude()) : null);
        transaction.setPlaceId(template.getPlaceId());
        transaction.setLocationSource(template.getLocationSource());

        if (template.getAmount() != null) {
            transaction.setAmount(template.getAmount());
            transaction.setOriginalAmount(template.getAmount());
            transaction.setOriginalCurrency(template.getCurrency());

            if (template.getCurrency() != null) {
                String baseCurrency = getBaseCurrency(template.getUserId());
                
                if (!template.getCurrency().equals(baseCurrency)) {
                    try {
                        var rateResponse = fxRateService.getRate(
                                template.getCurrency(), baseCurrency, LocalDate.now());
                        BigDecimal rate = rateResponse.rate();
                        transaction.setRateToBase(rate);
                        transaction.setBaseAmount(template.getAmount().multiply(rate));
                    } catch (Exception e) {
                        log.warn("Failed to get FX rate for {}: {}", template.getCurrency(), e.getMessage());
                        transaction.setRateToBase(BigDecimal.ONE);
                        transaction.setBaseAmount(template.getAmount());
                    }
                } else {
                    transaction.setRateToBase(BigDecimal.ONE);
                    transaction.setBaseAmount(template.getAmount());
                }
            } else {
                transaction.setRateToBase(BigDecimal.ONE);
                transaction.setBaseAmount(template.getAmount());
            }
        }

        transactionRepository.save(transaction);
    }

    private String getBaseCurrency(Long userId) {
        Optional<String> currency = userRepository.findById(userId)
                .map(u -> u.getBaseCurrency());
        return currency.orElse("RUB");
    }
}
