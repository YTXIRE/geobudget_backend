package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.transaction.TransactionCreateRequest;
import com.geobudget.geobudget.dto.transaction.TransactionCategoryDto;
import com.geobudget.geobudget.dto.transaction.TransactionResponse;
import com.geobudget.geobudget.dto.transaction.TransactionStatsResponse;
import com.geobudget.geobudget.dto.transaction.TransactionSummaryResponse;
import com.geobudget.geobudget.dto.transaction.TransactionUpdateRequest;
import com.geobudget.geobudget.dto.geoCompany.CountryAndCity;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.TransactionRepository;
import com.geobudget.geobudget.repository.TransactionStatsProjection;
import com.geobudget.geobudget.repository.TransactionSummaryProjection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final GeoIpService geoIpService;

    @Transactional
    public TransactionResponse create(Long userId, TransactionCreateRequest request) {
        return create(userId, request, null);
    }

    @Transactional
    public TransactionResponse create(Long userId, TransactionCreateRequest request, HttpServletRequest httpRequest) {
        Category category = validateBusinessRules(userId, request.getType(), request.getCategoryId());
        enrichLocationFromIp(request, httpRequest);

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .type(request.getType())
                .amount(request.getAmount())
                .category(category)
                .description(request.getDescription())
                .occurredAt(request.getOccurredAt())
                .isDeleted(false)
                .build();

        applyExtraFields(transaction, request);

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAll(
            Long userId,
            String type,
            LocalDateTime from,
            LocalDateTime to,
            Long categoryId,
            String city,
            String country,
            Pageable pageable
    ) {
        validatePeriod(from, to);

        if (type != null && !"income".equals(type) && !"expense".equals(type)) {
            throw new IllegalArgumentException("type must be income or expense");
        }

        if (categoryId != null) {
            resolveCategory(userId, categoryId);
        }

        Specification<Transaction> spec = buildSpec(userId, type, from, to, categoryId, city, country);

        return transactionRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getById(Long userId, Long id) {
        Transaction transaction = transactionRepository.findByIdAndUserIdAndIsDeletedFalse(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionResponse update(Long userId, Long id, TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findByIdAndUserIdAndIsDeletedFalse(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        Category category = validateBusinessRules(userId, request.getType(), request.getCategoryId());

        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setCategory(category);
        transaction.setDescription(request.getDescription());
        transaction.setOccurredAt(request.getOccurredAt());
        applyExtraFields(transaction, request);

        return mapToResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public void softDelete(Long userId, Long id) {
        Transaction transaction = transactionRepository.findByIdAndUserIdAndIsDeletedFalse(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        transaction.setIsDeleted(true);
        transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public TransactionSummaryResponse getSummary(Long userId, LocalDateTime from, LocalDateTime to) {
        validatePeriod(from, to);

        TransactionSummaryProjection projection = transactionRepository.getSummary(userId, from, to);
        BigDecimal income = projection != null && projection.getIncome() != null ? projection.getIncome() : BigDecimal.ZERO;
        BigDecimal expense = projection != null && projection.getExpense() != null ? projection.getExpense() : BigDecimal.ZERO;

        return TransactionSummaryResponse.builder()
                .income(income)
                .expense(expense)
                .balance(income.subtract(expense))
                .build();
    }

    @Transactional(readOnly = true)
    public TransactionStatsResponse getOverviewStats(Long userId) {
        return buildStatsResponse(userId, null);
    }

    @Transactional(readOnly = true)
    public TransactionStatsResponse getIncomeStats(Long userId) {
        return buildStatsResponse(userId, "income");
    }

    @Transactional(readOnly = true)
    public TransactionStatsResponse getExpenseStats(Long userId) {
        return buildStatsResponse(userId, "expense");
    }

    private TransactionStatsResponse buildStatsResponse(Long userId, String type) {
        if (type != null && !"income".equals(type) && !"expense".equals(type)) {
            throw new IllegalArgumentException("type must be income or expense");
        }

        TransactionStatsProjection projection = transactionRepository.getStats(userId, type);
        BigDecimal total = projection != null && projection.getTotalAmount() != null
                ? projection.getTotalAmount()
                : BigDecimal.ZERO;
        Long count = projection != null && projection.getCount() != null ? projection.getCount() : 0L;

        return TransactionStatsResponse.builder()
                .totalAmount(total)
                .count(count)
                .build();
    }

    private Category validateBusinessRules(Long userId, String type, Long categoryId) {
        if (!"income".equals(type) && !"expense".equals(type)) {
            throw new IllegalArgumentException("type must be income or expense");
        }

        if (categoryId == null) {
            throw new IllegalArgumentException("categoryId is required");
        }

        Category category = resolveCategory(userId, categoryId);

        if (!type.equals(category.getTransactionType())) {
            throw new IllegalArgumentException("Transaction type does not match category type");
        }

        return category;
    }

    private void applyExtraFields(Transaction transaction, TransactionCreateRequest request) {
        validateGeoPair(request.getLatitude(), request.getLongitude());

        transaction.setLatitude(request.getLatitude());
        transaction.setLongitude(request.getLongitude());
        transaction.setCity(request.getCity());
        transaction.setCountry(request.getCountry());
        transaction.setRegion(request.getRegion());
        transaction.setPlaceId(request.getPlaceId());
        transaction.setLocationSource(request.getLocationSource());
        transaction.setOriginalAmount(request.getOriginalAmount());
        transaction.setOriginalCurrency(request.getOriginalCurrency());
        transaction.setRateToBase(request.getRateToBase());
        transaction.setBaseAmount(request.getBaseAmount());
    }

    private void enrichLocationFromIp(TransactionCreateRequest request, HttpServletRequest httpRequest) {
        if (httpRequest == null || !isLocationMissing(request)) {
            return;
        }

        try {
            CountryAndCity location = geoIpService.getCityByExternalIp(httpRequest);
            if (location == null) {
                return;
            }

            if (location.getLatitude() != null) {
                request.setLatitude(BigDecimal.valueOf(location.getLatitude()));
            }

            if (location.getLongitude() != null) {
                request.setLongitude(BigDecimal.valueOf(location.getLongitude()));
            }

            request.setCity(location.getCity());
            request.setCountry(location.getCountry());
            request.setRegion(location.getRegion());
            request.setLocationSource("ip");
        } catch (Exception e) {
            log.warn("TransactionService.enrichLocationFromIp: failed to resolve IP location", e);
        }
    }

    private void applyExtraFields(Transaction transaction, TransactionUpdateRequest request) {
        validateGeoPair(request.getLatitude(), request.getLongitude());

        transaction.setLatitude(request.getLatitude());
        transaction.setLongitude(request.getLongitude());
        transaction.setCity(request.getCity());
        transaction.setCountry(request.getCountry());
        transaction.setRegion(request.getRegion());
        transaction.setPlaceId(request.getPlaceId());
        transaction.setLocationSource(request.getLocationSource());
        transaction.setOriginalAmount(request.getOriginalAmount());
        transaction.setOriginalCurrency(request.getOriginalCurrency());
        transaction.setRateToBase(request.getRateToBase());
        transaction.setBaseAmount(request.getBaseAmount());
    }

    private void validateGeoPair(BigDecimal latitude, BigDecimal longitude) {
        if ((latitude == null) != (longitude == null)) {
            throw new IllegalArgumentException("latitude and longitude must be provided together");
        }
    }

    private boolean isLocationMissing(TransactionCreateRequest request) {
        return request.getLatitude() == null
                && request.getLongitude() == null
                && isBlank(request.getCity())
                && isBlank(request.getCountry())
                && isBlank(request.getRegion())
                && isBlank(request.getPlaceId());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private Category resolveCategory(Long userId, Long categoryId) {
        if (categoryId == null) {
            return null;
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if ("system".equals(category.getType())) {
            return category;
        }

        if (!userId.equals(category.getUserId())) {
            throw new EntityNotFoundException("Category not found");
        }

        return category;
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .categoryId(transaction.getCategory() != null ? transaction.getCategory().getId() : null)
                .category(transaction.getCategory() == null ? null : TransactionCategoryDto.builder()
                        .id(transaction.getCategory().getId())
                        .name(transaction.getCategory().getName())
                        .transactionType(transaction.getCategory().getTransactionType())
                        .build())
                .description(transaction.getDescription())
                .occurredAt(transaction.getOccurredAt())
                .latitude(transaction.getLatitude())
                .longitude(transaction.getLongitude())
                .city(transaction.getCity())
                .country(transaction.getCountry())
                .region(transaction.getRegion())
                .placeId(transaction.getPlaceId())
                .locationSource(transaction.getLocationSource())
                .originalAmount(transaction.getOriginalAmount())
                .originalCurrency(transaction.getOriginalCurrency())
                .rateToBase(transaction.getRateToBase())
                .baseAmount(transaction.getBaseAmount())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    private void validatePeriod(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("from must be before or equal to to");
        }
    }

    private Specification<Transaction> buildSpec(
            Long userId,
            String type,
            LocalDateTime from,
            LocalDateTime to,
            Long categoryId,
            String city,
            String country
    ) {
        return (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
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

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (city != null && !city.isBlank()) {
                predicates.add(cb.equal(root.get("city"), city));
            }

            if (country != null && !country.isBlank()) {
                predicates.add(cb.equal(root.get("country"), country));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
