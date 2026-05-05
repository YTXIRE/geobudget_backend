package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.tag.TagDto;
import com.geobudget.geobudget.dto.transaction.TransactionCreateRequest;
import com.geobudget.geobudget.dto.transaction.TransactionCategoryDto;
import com.geobudget.geobudget.dto.transaction.TransactionResponse;
import com.geobudget.geobudget.dto.transaction.TransactionStatsResponse;
import com.geobudget.geobudget.dto.transaction.TransactionSummaryResponse;
import com.geobudget.geobudget.dto.transaction.TransactionUpdateRequest;
import com.geobudget.geobudget.dto.geoCompany.CountryAndCity;
import com.geobudget.geobudget.dto.fx.FxRateResponse;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.TagTransaction;
import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.PartnerRepository;
import com.geobudget.geobudget.repository.TagRepository;
import com.geobudget.geobudget.repository.TagTransactionRepository;
import com.geobudget.geobudget.repository.TransactionRepository;
import com.geobudget.geobudget.repository.TransactionStatsProjection;
import com.geobudget.geobudget.repository.TransactionSummaryProjection;
import com.geobudget.geobudget.repository.UserRepository;
import com.geobudget.geobudget.service.FcmService;
import com.geobudget.geobudget.service.PartnerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final GeoIpService geoIpService;
    private final UserRepository userRepository;
    private final FxRateService fxRateService;
    private final PartnerRepository partnerRepository;
    private final TagRepository tagRepository;
    private final TagTransactionRepository tagTransactionRepository;
    private FcmService fcmService;
    private PartnerService partnerService;

    @Autowired
    public void setFcmService(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @Autowired
    public void setPartnerService(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @Transactional
    public TransactionResponse create(Long userId, TransactionCreateRequest request) {
        return create(userId, request, null);
    }

    @Transactional
    public TransactionResponse create(Long userId, TransactionCreateRequest request, HttpServletRequest httpRequest) {
        User user = resolveUser(userId);
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

        applyExtraFields(transaction, request, user);

        Transaction savedTransaction = transactionRepository.save(transaction);

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                TagTransaction tt = TagTransaction.builder()
                        .tagId(tagId)
                        .transactionId(savedTransaction.getId())
                        .userId(userId)
                        .build();
                tagTransactionRepository.save(tt);
            }
        }

        notifyPartnersOfLargeExpense(userId, savedTransaction);

        return mapToResponse(savedTransaction, userId);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAll(
            Long userId,
            String type,
            LocalDateTime from,
            LocalDateTime to,
            Long categoryId,
            List<Long> tagIds,
            String city,
            String country,
            Long partnerId,
            boolean includePartners,
            Pageable pageable
    ) {
        validatePeriod(from, to);

        if (type != null && !"income".equals(type) && !"expense".equals(type) && !"transfer_to_goal".equals(type)) {
            throw new IllegalArgumentException("type must be income, expense or transfer_to_goal");
        }

        if (categoryId != null) {
            resolveCategory(userId, categoryId);
        }

        List<Long> visibleUserIds = includePartners ? getVisibleUserIds(userId) : List.of(userId);
        Specification<Transaction> spec = buildSpec(visibleUserIds, userId, type, from, to, categoryId, tagIds, city, country, partnerId);

        return transactionRepository.findAll(spec, pageable)
                .map(transaction -> mapToResponse(transaction, userId));
    }

    @Transactional(readOnly = true)
    public TransactionResponse getById(Long userId, Long id) {
        Transaction transaction = transactionRepository.findByIdAndUserIdAndIsDeletedFalse(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        return mapToResponse(transaction, userId);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getPartnerTransactions(
            Long userId,
            Long partnerId,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    ) {
        boolean hasPartnership = partnerRepository.existsAcceptedPartnership(userId, partnerId);
        if (!hasPartnership) {
            return Page.empty();
        }

        Specification<Transaction> spec = buildSpec(java.util.List.of(partnerId), userId, null, from, to, null, null, null, null, null);
        var result = transactionRepository.findAll(spec, pageable);
        
        return result.map(transaction -> mapToResponse(transaction, userId));
    }

    @Transactional
    public TransactionResponse update(Long userId, Long id, TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findByIdAndUserIdAndIsDeletedFalse(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        User user = resolveUser(userId);

        Category category = validateBusinessRules(userId, request.getType(), request.getCategoryId());

        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setCategory(category);
        transaction.setDescription(request.getDescription());
        transaction.setOccurredAt(request.getOccurredAt());
        applyExtraFields(transaction, request, user);

        tagTransactionRepository.deleteByTransactionIdAndUserId(id, userId);
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            for (Long tagId : request.getTagIds()) {
                TagTransaction tt = TagTransaction.builder()
                        .tagId(tagId)
                        .transactionId(id)
                        .userId(userId)
                        .build();
                tagTransactionRepository.save(tt);
            }
        }

        return mapToResponse(transactionRepository.save(transaction), userId);
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

        Specification<Transaction> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), to));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        List<Transaction> transactions = transactionRepository.findAll(spec);
        BigDecimal income = transactions.stream()
                .filter(t -> "income".equals(t.getType()))
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expense = transactions.stream()
                .filter(t -> "expense".equals(t.getType()))
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

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

        Specification<Transaction> spec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        List<Transaction> transactions = transactionRepository.findAll(spec);
        BigDecimal total = transactions.stream()
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Long count = (long) transactions.size();

        return TransactionStatsResponse.builder()
                .totalAmount(total)
                .count(count)
                .build();
    }

    private Category validateBusinessRules(Long userId, String type, Long categoryId) {
        if ("transfer_to_goal".equals(type)) {
            return null;
        }

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

    private void applyExtraFields(Transaction transaction, TransactionCreateRequest request, User user) {
        validateGeoPair(request.getLatitude(), request.getLongitude());

        transaction.setLatitude(request.getLatitude());
        transaction.setLongitude(request.getLongitude());
        transaction.setCity(request.getCity());
        transaction.setCountry(request.getCountry());
        transaction.setRegion(request.getRegion());
        transaction.setPlaceId(request.getPlaceId());
        transaction.setPlaceName(request.getPlaceName());
        transaction.setLocationSource(request.getLocationSource());
        applyCurrencyFields(
                transaction,
                request.getAmount(),
                request.getOriginalAmount(),
                request.getOriginalCurrency(),
                request.getRateToBase(),
                request.getBaseAmount(),
                request.getOccurredAt(),
                user
        );
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

    private void applyExtraFields(Transaction transaction, TransactionUpdateRequest request, User user) {
        validateGeoPair(request.getLatitude(), request.getLongitude());

        transaction.setLatitude(request.getLatitude());
        transaction.setLongitude(request.getLongitude());
        transaction.setCity(request.getCity());
        transaction.setCountry(request.getCountry());
        transaction.setRegion(request.getRegion());
        transaction.setPlaceId(request.getPlaceId());
        transaction.setPlaceName(request.getPlaceName());
        transaction.setLocationSource(request.getLocationSource());
        applyCurrencyFields(
                transaction,
                request.getAmount(),
                request.getOriginalAmount(),
                request.getOriginalCurrency(),
                request.getRateToBase(),
                request.getBaseAmount(),
                request.getOccurredAt(),
                user
        );
    }

    private void applyCurrencyFields(
            Transaction transaction,
            BigDecimal amount,
            BigDecimal originalAmountRaw,
            String originalCurrencyRaw,
            BigDecimal fallbackRate,
            BigDecimal fallbackBaseAmount,
            LocalDateTime occurredAt,
            User user
    ) {
        BigDecimal originalAmount = originalAmountRaw != null ? originalAmountRaw : amount;
        String baseCurrency = normalizeCurrency(user.getBaseCurrency(), "RUB");
        String originalCurrency = normalizeCurrency(originalCurrencyRaw, baseCurrency);

        BigDecimal rateToBase;
        BigDecimal baseAmount;

        if (originalCurrency.equals(baseCurrency)) {
            rateToBase = BigDecimal.ONE.setScale(6, RoundingMode.HALF_UP);
            baseAmount = originalAmount.setScale(2, RoundingMode.HALF_UP);
        } else {
            try {
                FxRateResponse rateResponse = fxRateService.getRate(
                        originalCurrency,
                        baseCurrency,
                        occurredAt != null ? occurredAt.toLocalDate() : null
                );
                rateToBase = rateResponse.rate();
                baseAmount = originalAmount.multiply(rateToBase).setScale(2, RoundingMode.HALF_UP);
            } catch (Exception e) {
                if (fallbackRate != null && fallbackBaseAmount != null) {
                    log.warn("TransactionService.applyCurrencyFields: fallback to client FX values for {} -> {}", originalCurrency, baseCurrency, e);
                    rateToBase = fallbackRate.setScale(6, RoundingMode.HALF_UP);
                    baseAmount = fallbackBaseAmount.setScale(2, RoundingMode.HALF_UP);
                } else {
                    throw new IllegalStateException("Не удалось получить курс валют для " + originalCurrency + " -> " + baseCurrency, e);
                }
            }
        }

        transaction.setOriginalAmount(originalAmount.setScale(2, RoundingMode.HALF_UP));
        transaction.setOriginalCurrency(originalCurrency);
        transaction.setRateToBase(rateToBase);
        transaction.setBaseAmount(baseAmount);
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

    private String normalizeCurrency(String currency, String fallback) {
        String normalized = currency == null || currency.isBlank()
                ? fallback
                : currency.trim().toUpperCase(Locale.ROOT);

        if (!normalized.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("currency must be ISO-4217 (3 uppercase letters)");
        }

        return normalized;
    }

    private User resolveUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
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

    private TransactionResponse mapToResponse(Transaction transaction, Long viewerUserId) {
        final List<TagDto> tags = tagTransactionRepository
                .findByTransactionIdAndUserId(transaction.getId(), transaction.getUserId())
                .stream()
                .map(tt -> tagRepository.findByIdAndUserId(tt.getTagId(), transaction.getUserId()))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(tag -> TagDto.builder()
                        .id(tag.getId())
                        .name(tag.getName())
                        .color(tag.getColor())
                        .icon(tag.getIcon())
                        .build())
                .collect(Collectors.toList());

        return TransactionResponse.builder()
                .id(transaction.getId())
                .userId(transaction.getUserId())
                .ownerName(resolveOwnerName(transaction.getUserId()))
                .editable(transaction.getUserId().equals(viewerUserId))
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .categoryId(transaction.getCategory() != null ? transaction.getCategory().getId() : null)
                .category(transaction.getCategory() == null ? null : TransactionCategoryDto.builder()
                        .id(transaction.getCategory().getId())
                        .name(transaction.getCategory().getName())
                        .transactionType(transaction.getCategory().getTransactionType())
                        .groupName(transaction.getCategory().getGroup() != null ? transaction.getCategory().getGroup().getName() : null)
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
                .tags(tags)
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    private List<Long> getVisibleUserIds(Long userId) {
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        userIds.addAll(partnerRepository.findAllAcceptedForUser(userId).stream()
                .map(partner -> partner.getUserId().equals(userId) ? partner.getPartnerId() : partner.getUserId())
                .distinct()
                .toList());
        return userIds;
    }

    private String resolveOwnerName(Long ownerId) {
        return userRepository.findById(ownerId)
                .map(User::getUsername)
                .orElse("Пользователь");
    }

    private void validatePeriod(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("from must be before or equal to to");
        }
    }

    private Specification<Transaction> buildSpec(
            List<Long> visibleUserIds,
            Long viewerUserId,
            String type,
            LocalDateTime from,
            LocalDateTime to,
            Long categoryId,
            List<Long> tagIds,
            String city,
            String country,
            Long partnerId
    ) {
        return (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            
            if (partnerId != null) {
                predicates.add(cb.equal(root.get("userId"), partnerId));
            } else {
                predicates.add(root.get("userId").in(visibleUserIds));
            }
            
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

            if (tagIds != null && !tagIds.isEmpty()) {
                var subquery = query.subquery(Long.class);
                var tagTransactionRoot = subquery.from(TagTransaction.class);
                subquery.select(tagTransactionRoot.get("transactionId"));
                subquery.where(
                        cb.equal(tagTransactionRoot.get("transactionId"), root.get("id")),
                        cb.equal(tagTransactionRoot.get("userId"), viewerUserId),
                        tagTransactionRoot.get("tagId").in(tagIds)
                );
                predicates.add(cb.exists(subquery));
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

    private void notifyPartnersOfLargeExpense(Long userId, Transaction transaction) {
        if (fcmService == null || partnerService == null) return;
        if (!"expense".equals(transaction.getType())) return;
        
        BigDecimal amount = transaction.getBaseAmount() != null 
            ? transaction.getBaseAmount() 
            : transaction.getAmount();
        if (amount == null) return;
        
        BigDecimal threshold = new BigDecimal("10000");
        if (amount.compareTo(threshold) < 0) return;
        
        List<Long> partnerIds = partnerRepository.findAllAcceptedForUser(userId).stream()
            .map(p -> p.getUserId().equals(userId) ? p.getPartnerId() : p.getUserId())
            .collect(Collectors.toList());
        
        User user = userRepository.findById(userId).orElse(null);
        String userName = user != null ? user.getUsername() : "Пользователь";
        
        BigDecimal roundedAmount = amount.setScale(0, RoundingMode.HALF_UP);
        
        for (Long partnerId : partnerIds) {
            fcmService.sendNotification(
                partnerId,
                "Крупная трата партнёра",
                String.format("%s потратил %s", userName, roundedAmount)
            );
        }
    }
}
