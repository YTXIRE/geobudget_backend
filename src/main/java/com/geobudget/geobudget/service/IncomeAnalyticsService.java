package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.analytics.*;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeAnalyticsService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public IncomeSummaryResponse getSummary(Long userId, String periodType, int year, Integer month) {
        LocalDate startDate;
        LocalDate endDate;

        switch (periodType.toUpperCase()) {
            case "MONTH" -> {
                startDate = LocalDate.of(year, month, 1);
                endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
            }
            case "QUARTER" -> {
                int quarter = (month - 1) / 3 + 1;
                startDate = LocalDate.of(year, (quarter - 1) * 3 + 1, 1);
                endDate = startDate.plusMonths(3).minusDays(1);
            }
            case "YEAR" -> {
                startDate = LocalDate.of(year, 1, 1);
                endDate = LocalDate.of(year, 12, 31);
            }
            default -> throw new IllegalArgumentException("Invalid period type: " + periodType);
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Transaction> transactions = transactionRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.equal(root.get("type"), "income"));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), startDateTime));
            predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), endDateTime));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        BigDecimal totalIncome = transactions.stream()
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = BigDecimal.ZERO;

        List<Transaction> expenseTransactions = transactionRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.equal(root.get("type"), "expense"));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), startDateTime));
            predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), endDateTime));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        totalExpense = expenseTransactions.stream()
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        BigDecimal savingsRate = BigDecimal.ZERO;
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            savingsRate = netBalance.multiply(BigDecimal.valueOf(100))
                    .divide(totalIncome, 2, RoundingMode.HALF_UP);
        }

        Map<Long, BigDecimal> byCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() != null ? t.getCategory().getId() : 0L,
                        Collectors.reducing(BigDecimal.ZERO,
                                t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount(),
                                BigDecimal::add)
                ));

        List<IncomeSummaryResponse.CategoryBreakdown> topCategories = byCategory.entrySet().stream()
                .sorted(Map.Entry.<Long, BigDecimal>comparingByValue().reversed())
                .limit(5)
                .map(entry -> {
                    String categoryName = transactions.stream()
                            .filter(t -> t.getCategory() != null && t.getCategory().getId().equals(entry.getKey()))
                            .findFirst()
                            .map(t -> t.getCategory().getName())
                            .orElse("Без категории");
                    BigDecimal percentage = totalIncome.compareTo(BigDecimal.ZERO) > 0
                            ? entry.getValue().multiply(BigDecimal.valueOf(100))
                                    .divide(totalIncome, 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;
                    return new IncomeSummaryResponse.CategoryBreakdown(categoryName, entry.getValue(), percentage);
                })
                .collect(Collectors.toList());

        Map<String, BigDecimal> byPartner = transactions.stream()
                .filter(t -> t.getDescription() != null && !t.getDescription().isEmpty())
                .collect(Collectors.groupingBy(
                        t -> t.getDescription().length() > 50 ? t.getDescription().substring(0, 50) : t.getDescription(),
                        Collectors.reducing(BigDecimal.ZERO,
                                t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount(),
                                BigDecimal::add)
                ));

        List<IncomeSummaryResponse.PartnerBreakdown> topPartners = byPartner.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, BigDecimal>::getValue, Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> new IncomeSummaryResponse.PartnerBreakdown(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        BigDecimal dailyAverage = totalIncome.divide(
                BigDecimal.valueOf(java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1),
                2, RoundingMode.HALF_UP);

        BigDecimal incomeGrowth = calculateIncomeGrowth(userId, year, month, periodType);

        return new IncomeSummaryResponse(
                new IncomeSummaryResponse.PeriodInfo(periodType, startDate, endDate),
                totalIncome.setScale(2, RoundingMode.HALF_UP),
                totalExpense.setScale(2, RoundingMode.HALF_UP),
                netBalance.setScale(2, RoundingMode.HALF_UP),
                savingsRate,
                incomeGrowth,
                topCategories,
                topPartners,
                dailyAverage,
                (long) transactions.size()
        );
    }

    @Transactional(readOnly = true)
    public IncomeTrendsResponse getTrends(Long userId, int months) {
        List<IncomeTrendsResponse.MonthlyData> trends = new ArrayList<>();
        YearMonth current = YearMonth.now();

        for (int i = months - 1; i >= 0; i--) {
            YearMonth month = current.minusMonths(i);
            LocalDateTime startDate = month.atDay(1).atStartOfDay();
            LocalDateTime endDate = month.atEndOfMonth().atTime(23, 59, 59);

            BigDecimal income = getIncomeForPeriod(userId, startDate, endDate);
            BigDecimal expense = getExpenseForPeriod(userId, startDate, endDate);
            BigDecimal net = income.subtract(expense);

            trends.add(new IncomeTrendsResponse.MonthlyData(
                    month.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                    income.setScale(2, RoundingMode.HALF_UP),
                    expense.setScale(2, RoundingMode.HALF_UP),
                    net.setScale(2, RoundingMode.HALF_UP)
            ));
        }

        BigDecimal avgIncome = trends.stream()
                .map(IncomeTrendsResponse.MonthlyData::income)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(trends.size()), 2, RoundingMode.HALF_UP);

        BigDecimal avgExpense = trends.stream()
                .map(IncomeTrendsResponse.MonthlyData::expense)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(trends.size()), 2, RoundingMode.HALF_UP);

        BigDecimal avgNet = trends.stream()
                .map(IncomeTrendsResponse.MonthlyData::net)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(trends.size()), 2, RoundingMode.HALF_UP);

        var bestMonth = trends.stream()
                .max(Comparator.comparing(IncomeTrendsResponse.MonthlyData::net))
                .map(m -> new IncomeTrendsResponse.TrendsSummary.BestWorstMonth(m.month(), m.net()))
                .orElse(null);

        var worstMonth = trends.stream()
                .min(Comparator.comparing(IncomeTrendsResponse.MonthlyData::net))
                .map(m -> new IncomeTrendsResponse.TrendsSummary.BestWorstMonth(m.month(), m.net()))
                .orElse(null);

        IncomeTrendsResponse.TrendsSummary summary = new IncomeTrendsResponse.TrendsSummary(
                avgIncome, avgExpense, avgNet, bestMonth, worstMonth
        );

        BigDecimal firstHalf = trends.stream()
                .limit(trends.size() / 2)
                .map(IncomeTrendsResponse.MonthlyData::income)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal secondHalf = trends.stream()
                .skip(trends.size() / 2)
                .map(IncomeTrendsResponse.MonthlyData::income)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal growthRate = BigDecimal.ZERO;
        if (firstHalf.compareTo(BigDecimal.ZERO) > 0) {
            growthRate = secondHalf.subtract(firstHalf)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(firstHalf, 2, RoundingMode.HALF_UP);
        }

        return new IncomeTrendsResponse(trends, summary, growthRate);
    }

    @Transactional(readOnly = true)
    public IncomeForecastResponse getForecast(Long userId, int forecastMonths) {
        List<IncomeTrendsResponse.MonthlyData> lastMonths = new ArrayList<>();
        YearMonth current = YearMonth.now();

        for (int i = 5; i >= 0; i--) {
            YearMonth month = current.minusMonths(i);
            LocalDateTime startDate = month.atDay(1).atStartOfDay();
            LocalDateTime endDate = month.atEndOfMonth().atTime(23, 59, 59);
            BigDecimal income = getIncomeForPeriod(userId, startDate, endDate);
            lastMonths.add(new IncomeTrendsResponse.MonthlyData(
                    month.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                    income,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            ));
        }

        List<BigDecimal> last6Incomes = lastMonths.stream()
                .map(IncomeTrendsResponse.MonthlyData::income)
                .collect(Collectors.toList());

        BigDecimal movingAvg = last6Incomes.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(6), 2, RoundingMode.HALF_UP);

        BigDecimal variance = BigDecimal.ZERO;
        for (BigDecimal income : last6Incomes) {
            variance = variance.add(income.subtract(movingAvg).pow(2));
        }
        variance = variance.divide(BigDecimal.valueOf(6), 2, RoundingMode.HALF_UP);
        BigDecimal stdDev = BigDecimal.valueOf(Math.sqrt(variance.doubleValue()));

        List<IncomeForecastResponse.ForecastMonth> forecast = new ArrayList<>();
        BigDecimal annualProjection = BigDecimal.ZERO;

        for (int i = 1; i <= forecastMonths; i++) {
            YearMonth month = current.plusMonths(i);
            BigDecimal confidence = BigDecimal.valueOf(1.0)
                    .subtract(BigDecimal.valueOf(i * 0.03))
                    .max(BigDecimal.valueOf(0.5));
            BigDecimal predicted = movingAvg.add(
                    movingAvg.multiply(stdDev.divide(movingAvg, 4, RoundingMode.HALF_UP))
                            .multiply(BigDecimal.valueOf(0.1 * i))
            );

            forecast.add(new IncomeForecastResponse.ForecastMonth(
                    month.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                    predicted.setScale(2, RoundingMode.HALF_UP),
                    confidence.setScale(2, RoundingMode.HALF_UP)
            ));

            annualProjection = annualProjection.add(predicted);
        }

        return new IncomeForecastResponse(
                forecast,
                "moving_average_3month",
                annualProjection.setScale(2, RoundingMode.HALF_UP),
                6
        );
    }

    @Transactional(readOnly = true)
    public IncomeComparisonResponse compareYears(Long userId, int currentYear, int previousYear) {
        List<BigDecimal> currentIncome = new ArrayList<>();
        List<BigDecimal> previousIncome = new ArrayList<>();
        BigDecimal currentTotal = BigDecimal.ZERO;
        BigDecimal previousTotal = BigDecimal.ZERO;
        BigDecimal currentBest = BigDecimal.ZERO;
        Integer currentBestMonth = 1;
        BigDecimal previousBest = BigDecimal.ZERO;
        Integer previousBestMonth = 1;

        for (int month = 1; month <= 12; month++) {
            YearMonth ym = YearMonth.of(currentYear, month);
            LocalDateTime start = ym.atDay(1).atStartOfDay();
            LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);
            BigDecimal income = getIncomeForPeriod(userId, start, end);
            currentIncome.add(income);
            currentTotal = currentTotal.add(income);
            if (income.compareTo(currentBest) > 0) {
                currentBest = income;
                currentBestMonth = month;
            }

            ym = YearMonth.of(previousYear, month);
            start = ym.atDay(1).atStartOfDay();
            end = ym.atEndOfMonth().atTime(23, 59, 59);
            income = getIncomeForPeriod(userId, start, end);
            previousIncome.add(income);
            previousTotal = previousTotal.add(income);
            if (income.compareTo(previousBest) > 0) {
                previousBest = income;
                previousBestMonth = month;
            }
        }

        var currentYearData = new IncomeComparisonResponse.YearData(
                currentYear,
                currentTotal.setScale(2, RoundingMode.HALF_UP),
                currentTotal.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP),
                new IncomeComparisonResponse.YearData.TopMonth(currentBestMonth, currentBest),
                currentIncome.stream().map(i -> i.setScale(2, RoundingMode.HALF_UP)).collect(Collectors.toList())
        );

        var previousYearData = new IncomeComparisonResponse.YearData(
                previousYear,
                previousTotal.setScale(2, RoundingMode.HALF_UP),
                previousTotal.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP),
                new IncomeComparisonResponse.YearData.TopMonth(previousBestMonth, previousBest),
                previousIncome.stream().map(i -> i.setScale(2, RoundingMode.HALF_UP)).collect(Collectors.toList())
        );

        BigDecimal absoluteChange = currentTotal.subtract(previousTotal);
        BigDecimal percentageChange = previousTotal.compareTo(BigDecimal.ZERO) > 0
                ? absoluteChange.multiply(BigDecimal.valueOf(100))
                        .divide(previousTotal, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        var comparison = new IncomeComparisonResponse.Comparison(
                absoluteChange.setScale(2, RoundingMode.HALF_UP),
                percentageChange,
                absoluteChange.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP),
                currentBest.subtract(previousBest).setScale(2, RoundingMode.HALF_UP),
                percentageChange.compareTo(BigDecimal.ZERO) > 0
        );

        return new IncomeComparisonResponse(currentYearData, previousYearData, comparison);
    }

    @Transactional(readOnly = true)
    public IncomeByCategoryResponse getByCategory(Long userId, String periodType, int year, Integer month) {
        LocalDateTime startDate;
        LocalDateTime endDate = LocalDateTime.now();

        switch (periodType.toUpperCase()) {
            case "MONTH" -> {
                YearMonth ym = YearMonth.of(year, month);
                startDate = ym.atDay(1).atStartOfDay();
                endDate = ym.atEndOfMonth().atTime(23, 59, 59);
            }
            case "QUARTER" -> {
                int quarter = (month - 1) / 3 + 1;
                startDate = YearMonth.of(year, (quarter - 1) * 3 + 1).atDay(1).atStartOfDay();
                endDate = YearMonth.of(year, quarter * 3).atEndOfMonth().atTime(23, 59, 59);
            }
            case "YEAR" -> {
                startDate = LocalDate.of(year, 1, 1).atStartOfDay();
                endDate = LocalDate.of(year, 12, 31).atTime(23, 59, 59);
            }
            default -> throw new IllegalArgumentException("Invalid period type: " + periodType);
        }

        final LocalDateTime finalStartDate = startDate;
        final LocalDateTime finalEndDate = endDate;

        List<Transaction> transactions = transactionRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.equal(root.get("type"), "income"));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), finalStartDate));
            predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), finalEndDate));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        BigDecimal totalIncome = transactions.stream()
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Long, List<Transaction>> byCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory() != null ? t.getCategory().getId() : 0L
                ));

        List<IncomeByCategoryResponse.CategoryIncome> categories = byCategory.entrySet().stream()
                .map(entry -> {
                    Transaction first = entry.getValue().get(0);
                    String categoryName = first.getCategory() != null
                            ? first.getCategory().getName() : "Без категории";
                    String icon = first.getCategory() != null && first.getCategory().getIcon() != null
                            ? first.getCategory().getIcon().getName() : null;
                    String color = first.getCategory() != null && first.getCategory().getColor() != null
                            ? first.getCategory().getColor().getHex() : "#4CAF50";

                    BigDecimal amount = entry.getValue().stream()
                            .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal percentage = totalIncome.compareTo(BigDecimal.ZERO) > 0
                            ? amount.multiply(BigDecimal.valueOf(100))
                                    .divide(totalIncome, 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    return new IncomeByCategoryResponse.CategoryIncome(
                            entry.getKey(),
                            categoryName,
                            icon,
                            color,
                            amount.setScale(2, RoundingMode.HALF_UP),
                            percentage,
                            BigDecimal.ZERO
                    );
                })
                .sorted(Comparator.comparing(IncomeByCategoryResponse.CategoryIncome::amount).reversed())
                .collect(Collectors.toList());

        return new IncomeByCategoryResponse(categories, totalIncome.setScale(2, RoundingMode.HALF_UP));
    }

    private BigDecimal getIncomeForPeriod(Long userId, LocalDateTime from, LocalDateTime to) {
        List<Transaction> transactions = transactionRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.equal(root.get("type"), "income"));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), from));
            predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), to));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        return transactions.stream()
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getExpenseForPeriod(Long userId, LocalDateTime from, LocalDateTime to) {
        List<Transaction> transactions = transactionRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.equal(root.get("type"), "expense"));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), from));
            predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), to));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });

        return transactions.stream()
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateIncomeGrowth(Long userId, int year, int month, String periodType) {
        LocalDateTime currentStart;
        LocalDateTime currentEnd;
        LocalDateTime previousStart;
        LocalDateTime previousEnd;

        YearMonth currentYM = YearMonth.of(year, month);
        YearMonth previousYM;

        switch (periodType.toUpperCase()) {
            case "MONTH" -> {
                previousYM = currentYM.minusMonths(1);
            }
            case "QUARTER" -> {
                int quarter = (month - 1) / 3 + 1;
                int prevQuarter = quarter == 1 ? 4 : quarter - 1;
                int prevYear = quarter == 1 ? year - 1 : year;
                previousYM = YearMonth.of(prevYear, (prevQuarter - 1) * 3 + 1);
            }
            case "YEAR" -> {
                previousYM = YearMonth.of(year - 1, 1);
            }
            default -> {
                return BigDecimal.ZERO;
            }
        }

        YearMonth currentEndYM;
        YearMonth previousEndYM;

        switch (periodType.toUpperCase()) {
            case "MONTH" -> {
                currentStart = currentYM.atDay(1).atStartOfDay();
                currentEnd = currentYM.atEndOfMonth().atTime(23, 59, 59);
                previousStart = previousYM.atDay(1).atStartOfDay();
                previousEnd = previousYM.atEndOfMonth().atTime(23, 59, 59);
            }
            case "QUARTER" -> {
                currentStart = currentYM.atDay(1).atStartOfDay();
                currentEnd = currentYM.plusMonths(2).atEndOfMonth().atTime(23, 59, 59);
                previousStart = previousYM.atDay(1).atStartOfDay();
                previousEnd = previousYM.plusMonths(2).atEndOfMonth().atTime(23, 59, 59);
            }
            case "YEAR" -> {
                currentStart = currentYM.atDay(1).atStartOfDay();
                currentEnd = currentYM.atEndOfMonth().atTime(23, 59, 59);
                previousStart = previousYM.atDay(1).atStartOfDay();
                previousEnd = previousYM.atEndOfMonth().atTime(23, 59, 59);
            }
            default -> {
                return BigDecimal.ZERO;
            }
        }

        BigDecimal currentIncome = getIncomeForPeriod(userId, currentStart, currentEnd);
        BigDecimal previousIncome = getIncomeForPeriod(userId, previousStart, previousEnd);

        if (previousIncome.compareTo(BigDecimal.ZERO) > 0) {
            return currentIncome.subtract(previousIncome)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(previousIncome, 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
