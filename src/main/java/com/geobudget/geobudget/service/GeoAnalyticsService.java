package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.analytics.GeoCityAnalyticsDetailResponse;
import com.geobudget.geobudget.dto.analytics.GeoCityAnalyticsResponse;
import com.geobudget.geobudget.dto.analytics.GeoCityCategoryAnalyticsResponse;
import com.geobudget.geobudget.dto.analytics.GeoCityTransactionAnalyticsResponse;
import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeoAnalyticsService {
    private static final int GEO_BUCKET_SCALE = 2;
    private static final int CENTER_SCALE = 6;

    private final TransactionRepository transactionRepository;

    public GeoAnalyticsService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<GeoCityAnalyticsResponse> getCities(Long userId, String type, LocalDateTime from, LocalDateTime to) {
        validateFilters(type, from, to);

        return buildGroups(userId, type, from, to).values().stream()
                .filter(CityAggregate::hasCoordinates)
                .sorted(Comparator
                        .comparing(CityAggregate::getLatestOccurredAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(CityAggregate::getLabel))
                .map(this::toCityResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GeoCityAnalyticsDetailResponse getCityDetail(Long userId, String key, String type, LocalDateTime from, LocalDateTime to) {
        validateFilters(type, from, to);

        CityAggregate aggregate = buildGroups(userId, type, from, to).get(key);
        if (aggregate == null || !aggregate.hasCoordinates()) {
            throw new EntityNotFoundException("Geo city analytics not found");
        }

        List<GeoCityCategoryAnalyticsResponse> categories = buildCategoryResponses(aggregate.getTransactions());
        List<GeoCityTransactionAnalyticsResponse> transactions = aggregate.getTransactions().stream()
                .sorted(Comparator.comparing(
                        (Transaction transaction) -> transaction.getOccurredAt(),
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .map(this::toTransactionResponse)
                .toList();

        return new GeoCityAnalyticsDetailResponse(
                aggregate.getKey(),
                aggregate.getLabel(),
                aggregate.getCity(),
                aggregate.getCountry(),
                aggregate.getRegion(),
                aggregate.getLatitudeCenter(),
                aggregate.getLongitudeCenter(),
                aggregate.getTransactionCount(),
                aggregate.getIncomeTotal(),
                aggregate.getExpenseTotal(),
                aggregate.getBalance(),
                categories,
                transactions
        );
    }

    private Map<String, CityAggregate> buildGroups(Long userId, String type, LocalDateTime from, LocalDateTime to) {
        List<Transaction> transactions = transactionRepository.findAll(buildSpec(userId, type, from, to));
        Map<String, CityAggregate> groups = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            CityKey cityKey = buildCityKey(transaction);
            if (cityKey == null) {
                continue;
            }

            groups.computeIfAbsent(cityKey.key(), ignored -> new CityAggregate(cityKey)).add(transaction);
        }

        return groups;
    }

    private List<GeoCityCategoryAnalyticsResponse> buildCategoryResponses(List<Transaction> transactions) {
        Map<String, CategoryAggregate> categories = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            Long categoryId = transaction.getCategory() != null ? transaction.getCategory().getId() : null;
            String categoryName = transaction.getCategory() != null && transaction.getCategory().getName() != null
                    ? transaction.getCategory().getName()
                    : "Без категории";
            String categoryKey = categoryId != null ? categoryId.toString() : "unknown:" + normalize(categoryName);

            categories.computeIfAbsent(
                    categoryKey,
                    ignored -> new CategoryAggregate(categoryId, categoryName)
            ).add(transaction);
        }

        return categories.values().stream()
                .sorted(Comparator
                        .comparing(CategoryAggregate::getExpenseTotal, Comparator.reverseOrder())
                        .thenComparing(CategoryAggregate::getIncomeTotal, Comparator.reverseOrder())
                        .thenComparing(CategoryAggregate::getCategoryName))
                .map(category -> new GeoCityCategoryAnalyticsResponse(
                        category.getCategoryId(),
                        category.getCategoryName(),
                        category.getTransactionCount(),
                        category.getIncomeTotal(),
                        category.getExpenseTotal(),
                        category.getBalance()
                ))
                .toList();
    }

    private GeoCityAnalyticsResponse toCityResponse(CityAggregate aggregate) {
        return new GeoCityAnalyticsResponse(
                aggregate.getKey(),
                aggregate.getLabel(),
                aggregate.getCity(),
                aggregate.getCountry(),
                aggregate.getRegion(),
                aggregate.getLatitudeCenter(),
                aggregate.getLongitudeCenter(),
                aggregate.getTransactionCount(),
                aggregate.getIncomeTotal(),
                aggregate.getExpenseTotal(),
                aggregate.getBalance()
        );
    }

    private GeoCityTransactionAnalyticsResponse toTransactionResponse(Transaction transaction) {
        return new GeoCityTransactionAnalyticsResponse(
                transaction.getId(),
                transaction.getType(),
                transaction.getCategory() != null ? transaction.getCategory().getId() : null,
                transaction.getCategory() != null ? transaction.getCategory().getName() : "Без категории",
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getBaseAmount(),
                transaction.getOccurredAt(),
                transaction.getLocationSource()
        );
    }

    private CityKey buildCityKey(Transaction transaction) {
        String city = blankToNull(transaction.getCity());
        String country = blankToNull(transaction.getCountry());
        String region = blankToNull(transaction.getRegion());

        if (city != null || country != null || region != null) {
            String normalizedCity = normalize(city);
            String normalizedCountry = normalize(country);
            String normalizedRegion = normalize(region);
            String label = joinLabel(city, region, country);

            return new CityKey(
                    "place::" + nullSafe(normalizedCountry) + "::" + nullSafe(normalizedRegion) + "::" + nullSafe(normalizedCity),
                    label,
                    city,
                    country,
                    region
            );
        }

        if (transaction.getLatitude() == null || transaction.getLongitude() == null) {
            return null;
        }

        BigDecimal latBucket = bucketCoordinate(transaction.getLatitude());
        BigDecimal lonBucket = bucketCoordinate(transaction.getLongitude());
        String label = "Координаты " + latBucket.toPlainString() + ", " + lonBucket.toPlainString();

        return new CityKey(
                "geo::" + latBucket.toPlainString() + "::" + lonBucket.toPlainString(),
                label,
                null,
                null,
                null
        );
    }

    private Specification<Transaction> buildSpec(Long userId, String type, LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.isFalse(root.get("isDeleted")));

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), from));
            }

            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void validateFilters(String type, LocalDateTime from, LocalDateTime to) {
        if (type != null && !"income".equals(type) && !"expense".equals(type)) {
            throw new IllegalArgumentException("type must be income or expense");
        }

        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("from must be before or equal to to");
        }
    }

    private BigDecimal bucketCoordinate(BigDecimal coordinate) {
        return coordinate.setScale(GEO_BUCKET_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal transactionValue(Transaction transaction) {
        return transaction.getBaseAmount() != null ? transaction.getBaseAmount() : transaction.getAmount();
    }

    private String joinLabel(String city, String region, String country) {
        List<String> parts = new ArrayList<>();
        if (city != null) {
            parts.add(city);
        }
        if (region != null) {
            parts.add(region);
        } else if (country != null) {
            parts.add(country);
        }
        return String.join(", ", parts);
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    private record CityKey(String key, String label, String city, String country, String region) {
    }

    private final class CityAggregate {
        private final String key;
        private final String label;
        private final String city;
        private final String country;
        private final String region;
        private final List<Transaction> transactions = new ArrayList<>();
        private BigDecimal incomeTotal = BigDecimal.ZERO;
        private BigDecimal expenseTotal = BigDecimal.ZERO;
        private BigDecimal latitudeSum = BigDecimal.ZERO;
        private BigDecimal longitudeSum = BigDecimal.ZERO;
        private long coordinateCount = 0;
        private long transactionCount = 0;
        private LocalDateTime latestOccurredAt;

        private CityAggregate(CityKey key) {
            this.key = key.key();
            this.label = key.label();
            this.city = key.city();
            this.country = key.country();
            this.region = key.region();
        }

        private void add(Transaction transaction) {
            transactions.add(transaction);
            transactionCount += 1;

            BigDecimal value = transactionValue(transaction);
            if ("income".equals(transaction.getType())) {
                incomeTotal = incomeTotal.add(value);
            } else if ("expense".equals(transaction.getType())) {
                expenseTotal = expenseTotal.add(value);
            }

            if (transaction.getLatitude() != null && transaction.getLongitude() != null) {
                latitudeSum = latitudeSum.add(transaction.getLatitude());
                longitudeSum = longitudeSum.add(transaction.getLongitude());
                coordinateCount += 1;
            }

            if (transaction.getOccurredAt() != null &&
                    (latestOccurredAt == null || transaction.getOccurredAt().isAfter(latestOccurredAt))) {
                latestOccurredAt = transaction.getOccurredAt();
            }
        }

        private boolean hasCoordinates() {
            return coordinateCount > 0;
        }

        private String getKey() {
            return key;
        }

        private String getLabel() {
            return label;
        }

        private String getCity() {
            return city;
        }

        private String getCountry() {
            return country;
        }

        private String getRegion() {
            return region;
        }

        private List<Transaction> getTransactions() {
            return transactions;
        }

        private BigDecimal getLatitudeCenter() {
            if (!hasCoordinates()) {
                return null;
            }
            return latitudeSum.divide(BigDecimal.valueOf(coordinateCount), CENTER_SCALE, RoundingMode.HALF_UP);
        }

        private BigDecimal getLongitudeCenter() {
            if (!hasCoordinates()) {
                return null;
            }
            return longitudeSum.divide(BigDecimal.valueOf(coordinateCount), CENTER_SCALE, RoundingMode.HALF_UP);
        }

        private Long getTransactionCount() {
            return transactionCount;
        }

        private BigDecimal getIncomeTotal() {
            return incomeTotal;
        }

        private BigDecimal getExpenseTotal() {
            return expenseTotal;
        }

        private BigDecimal getBalance() {
            return incomeTotal.subtract(expenseTotal);
        }

        private LocalDateTime getLatestOccurredAt() {
            return latestOccurredAt;
        }
    }

    private final class CategoryAggregate {
        private final Long categoryId;
        private final String categoryName;
        private long transactionCount = 0;
        private BigDecimal incomeTotal = BigDecimal.ZERO;
        private BigDecimal expenseTotal = BigDecimal.ZERO;

        private CategoryAggregate(Long categoryId, String categoryName) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
        }

        private void add(Transaction transaction) {
            transactionCount += 1;
            BigDecimal value = transactionValue(transaction);
            if ("income".equals(transaction.getType())) {
                incomeTotal = incomeTotal.add(value);
            } else if ("expense".equals(transaction.getType())) {
                expenseTotal = expenseTotal.add(value);
            }
        }

        private Long getCategoryId() {
            return categoryId;
        }

        private String getCategoryName() {
            return categoryName;
        }

        private Long getTransactionCount() {
            return transactionCount;
        }

        private BigDecimal getIncomeTotal() {
            return incomeTotal;
        }

        private BigDecimal getExpenseTotal() {
            return expenseTotal;
        }

        private BigDecimal getBalance() {
            return incomeTotal.subtract(expenseTotal);
        }
    }
}
