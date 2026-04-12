package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.budget.BudgetProgressResponse;
import com.geobudget.geobudget.dto.budget.BudgetDailyPointResponse;
import com.geobudget.geobudget.dto.budget.BudgetTopTransactionResponse;
import com.geobudget.geobudget.dto.budget.BudgetRequest;
import com.geobudget.geobudget.dto.budget.BudgetResponse;
import com.geobudget.geobudget.entity.Budget;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Partner;
import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.repository.BudgetRepository;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.PartnerRepository;
import com.geobudget.geobudget.repository.TransactionRepository;
import com.geobudget.geobudget.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;

    @Transactional(readOnly = true)
    public List<BudgetResponse> getBudgets(Long userId) {
        List<Long> partnerIds = getAcceptedPartnerIds(userId);
        
        List<Budget> budgets = new ArrayList<>();
        budgets.addAll(budgetRepository.findByUserIdOrderByCreatedAtDesc(userId));
        
        for (Long partnerId : partnerIds) {
            budgets.addAll(budgetRepository.findByUserIdOrderByCreatedAtDesc(partnerId));
        }
        
        return budgets.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BudgetResponse getBudget(Long userId, Long id) {
        return toResponse(resolveBudget(userId, id));
    }

    @Transactional
    public BudgetResponse createBudget(Long userId, BudgetRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Budget budget = new Budget();
        applyRequest(budget, request, userId, user);
        budget.setUserId(userId);
        return toResponse(budgetRepository.save(budget));
    }

    @Transactional
    public BudgetResponse updateBudget(Long userId, Long id, BudgetRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Budget budget = resolveBudget(userId, id);
        applyRequest(budget, request, userId, user);
        return toResponse(budgetRepository.save(budget));
    }

    @Transactional
    public void deleteBudget(Long userId, Long id) {
        Budget budget = resolveBudget(userId, id);
        budgetRepository.delete(budget);
    }

    @Transactional(readOnly = true)
    public List<BudgetProgressResponse> getBudgetProgress(Long userId) {
        List<Long> partnerIds = getAcceptedPartnerIds(userId);
        
        List<Budget> allBudgets = new ArrayList<>();
        allBudgets.addAll(budgetRepository.findByUserIdOrderByCreatedAtDesc(userId));
        
        for (Long partnerId : partnerIds) {
            allBudgets.addAll(budgetRepository.findByUserIdOrderByCreatedAtDesc(partnerId));
        }
        
        return allBudgets.stream()
                .map(budget -> computeProgress(userId, budget))
                .toList();
    }

    @Transactional(readOnly = true)
    public BudgetProgressResponse getBudgetProgress(Long userId, Long id) {
        return computeProgress(userId, resolveBudget(userId, id));
    }

    private void applyRequest(Budget budget, BudgetRequest request, Long userId, User user) {
        validateRequest(request);
        budget.setName(request.getName().trim());
        budget.setPeriodType(request.getPeriodType());
        budget.setAmountLimit(request.getAmountLimit().setScale(2, RoundingMode.HALF_UP));
        budget.setBaseCurrency(
                request.getBaseCurrency() == null || request.getBaseCurrency().isBlank()
                        ? (user.getBaseCurrency() == null ? "RUB" : user.getBaseCurrency())
                        : request.getBaseCurrency().trim().toUpperCase()
        );
        budget.setScopeType(request.getScopeType());
        budget.setStartsAt(request.getStartsAt());
        budget.setEndsAt(request.getEndsAt());
        budget.setWarningThreshold(request.getWarningThreshold().setScale(2, RoundingMode.HALF_UP));
        budget.setIsActive(request.getIsActive() == null ? Boolean.TRUE : request.getIsActive());

        if (request.getPartnerId() != null) {
            if (!partnerRepository.existsAcceptedPartnership(userId, request.getPartnerId())) {
                throw new IllegalArgumentException("Cannot create shared budget with non-partner user");
            }
            budget.setPartnerId(request.getPartnerId());
        } else {
            budget.setPartnerId(null);
        }

        if ("category".equals(request.getScopeType())) {
            Category category = categoryRepository.findAccessibleById(request.getCategoryId(), userId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found or inaccessible"));
            budget.setCategory(category);
            budget.setRegion(null);
            budget.setCity(null);
            budget.setCountry(null);
        } else if ("region".equals(request.getScopeType())) {
            budget.setCategory(null);
            budget.setRegion(request.getRegion().trim());
            budget.setCity(null);
            budget.setCountry(null);
        } else if ("city".equals(request.getScopeType())) {
            budget.setCategory(null);
            budget.setRegion(request.getRegion() == null ? null : request.getRegion().trim());
            budget.setCity(request.getCity().trim());
            budget.setCountry(null);
        } else if ("country".equals(request.getScopeType())) {
            budget.setCategory(null);
            budget.setRegion(null);
            budget.setCity(null);
            budget.setCountry(request.getCountry() == null ? null : request.getCountry().trim());
        }
    }

    private void validateRequest(BudgetRequest request) {
        if (request.getStartsAt().isAfter(request.getEndsAt())) {
            throw new IllegalArgumentException("startsAt must be before or equal to endsAt");
        }
        if ("category".equals(request.getScopeType()) && request.getCategoryId() == null) {
            throw new IllegalArgumentException("categoryId is required for category budget");
        }
        if ("region".equals(request.getScopeType()) && (request.getRegion() == null || request.getRegion().isBlank())) {
            throw new IllegalArgumentException("region is required for region budget");
        }
        if ("city".equals(request.getScopeType()) && (request.getCity() == null || request.getCity().isBlank())) {
            throw new IllegalArgumentException("city is required for city budget");
        }
        if ("country".equals(request.getScopeType()) && (request.getCountry() == null || request.getCountry().isBlank())) {
            throw new IllegalArgumentException("country is required for country budget");
        }
    }

    private BudgetProgressResponse computeProgress(Long userId, Budget budget) {
        List<Transaction> transactions = transactionRepository.findAll(buildBudgetSpec(userId, budget));
        BigDecimal spent = transactions.stream()
                .map(tx -> tx.getBaseAmount() != null ? tx.getBaseAmount() : tx.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal limit = budget.getAmountLimit();
        BigDecimal remaining = limit.subtract(spent).setScale(2, RoundingMode.HALF_UP);
        BigDecimal ratio = limit.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : spent.divide(limit, 4, RoundingMode.HALF_UP);

        LocalDate today = LocalDate.now();
        int totalDays = (int) (budget.getEndsAt().toEpochDay() - budget.getStartsAt().toEpochDay() + 1);
        int elapsedDays = today.isBefore(budget.getStartsAt())
                ? 0
                : (int) Math.min(totalDays, today.toEpochDay() - budget.getStartsAt().toEpochDay() + 1);
        int remainingDays = Math.max(0, totalDays - elapsedDays);
        BigDecimal averageDailySpend = elapsedDays == 0
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : spent.divide(BigDecimal.valueOf(elapsedDays), 2, RoundingMode.HALF_UP);
        BigDecimal safeDailySpend = remainingDays == 0
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : remaining.max(BigDecimal.ZERO).divide(BigDecimal.valueOf(remainingDays), 2, RoundingMode.HALF_UP);
        BigDecimal projectedSpent = averageDailySpend.multiply(BigDecimal.valueOf(totalDays)).setScale(2, RoundingMode.HALF_UP);

        String status = ratio.compareTo(budget.getWarningThreshold()) < 0
                ? "ok"
                : ratio.compareTo(BigDecimal.ONE) <= 0
                ? "warning"
                : "exceeded";

        List<BudgetDailyPointResponse> dailySeries = buildDailySeries(budget, transactions);
        LocalDate projectedExceedDate = calculateProjectedExceedDate(budget, averageDailySpend);
        List<BudgetTopTransactionResponse> topTransactions = buildTopTransactions(transactions);

        return BudgetProgressResponse.builder()
                .budgetId(budget.getId())
                .name(budget.getName())
                .scopeType(budget.getScopeType())
                .scopeLabel(scopeLabel(budget))
                .amountLimit(limit)
                .spent(spent)
                .remaining(remaining)
                .progressRatio(ratio)
                .status(status)
                .matchedTransactionsCount((long) transactions.size())
                .baseCurrency(budget.getBaseCurrency())
                .dailySeries(dailySeries)
                .averageDailySpend(averageDailySpend)
                .safeDailySpend(safeDailySpend)
                .projectedSpent(projectedSpent)
                .projectedExceedDate(projectedExceedDate)
                .topTransactions(topTransactions)
                .build();
    }

    private LocalDate calculateProjectedExceedDate(Budget budget, BigDecimal averageDailySpend) {
        if (averageDailySpend.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        BigDecimal daysToExceed = budget.getAmountLimit()
                .divide(averageDailySpend, 0, RoundingMode.CEILING);

        LocalDate projectedDate = budget.getStartsAt().plusDays(daysToExceed.longValue() - 1);
        if (projectedDate.isAfter(budget.getEndsAt())) {
            return null;
        }
        return projectedDate;
    }

    private List<BudgetTopTransactionResponse> buildTopTransactions(List<Transaction> transactions) {
        return transactions.stream()
                .sorted(Comparator.comparing(
                        (Transaction tx) -> tx.getBaseAmount() != null ? tx.getBaseAmount() : tx.getAmount(),
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .limit(5)
                .map(tx -> BudgetTopTransactionResponse.builder()
                        .transactionId(tx.getId())
                        .categoryName(tx.getCategory() != null ? tx.getCategory().getName() : "Без категории")
                        .description(tx.getDescription())
                        .amount(tx.getBaseAmount() != null ? tx.getBaseAmount() : tx.getAmount())
                        .occurredAt(tx.getOccurredAt())
                        .build())
                .toList();
    }

    private List<BudgetDailyPointResponse> buildDailySeries(Budget budget, List<Transaction> transactions) {
        Map<LocalDate, BigDecimal> series = new LinkedHashMap<>();

        LocalDate cursor = budget.getStartsAt();
        while (!cursor.isAfter(budget.getEndsAt())) {
            series.put(cursor, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
            cursor = cursor.plusDays(1);
        }

        for (Transaction transaction : transactions) {
            if (transaction.getOccurredAt() == null) {
                continue;
            }

            LocalDate day = transaction.getOccurredAt().toLocalDate();
            BigDecimal amount = transaction.getBaseAmount() != null ? transaction.getBaseAmount() : transaction.getAmount();
            series.computeIfPresent(day, (ignored, current) -> current.add(amount).setScale(2, RoundingMode.HALF_UP));
        }

        return series.entrySet().stream()
                .map(entry -> BudgetDailyPointResponse.builder()
                        .date(entry.getKey())
                        .spent(entry.getValue())
                        .build())
                .toList();
    }

    private Specification<Transaction> buildBudgetSpec(Long userId, Budget budget) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (budget.getPartnerId() != null) {
                predicates.add(cb.or(
                        cb.equal(root.get("userId"), userId),
                        cb.equal(root.get("userId"), budget.getPartnerId())
                ));
            } else {
                predicates.add(cb.equal(root.get("userId"), userId));
            }
            
            predicates.add(cb.equal(root.get("type"), "expense"));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), budget.getStartsAt().atStartOfDay()));
            predicates.add(cb.lessThan(root.get("occurredAt"), budget.getEndsAt().plusDays(1).atStartOfDay()));

            switch (budget.getScopeType()) {
                case "category" -> predicates.add(cb.equal(root.get("category").get("id"), budget.getCategory().getId()));
                case "region" -> predicates.add(cb.equal(cb.lower(root.get("region")), budget.getRegion().toLowerCase()));
                case "city" -> predicates.add(cb.equal(cb.lower(root.get("city")), budget.getCity().toLowerCase()));
                case "country" -> predicates.add(cb.equal(cb.lower(root.get("country")), budget.getCountry().toLowerCase()));
                default -> throw new IllegalArgumentException("Unsupported scopeType: " + budget.getScopeType());
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private String scopeLabel(Budget budget) {
        return switch (budget.getScopeType()) {
            case "category" -> budget.getCategory() != null ? budget.getCategory().getName() : "Без категории";
            case "region" -> budget.getRegion();
            case "city" -> budget.getCity();
            case "country" -> budget.getCountry();
            default -> budget.getName();
        };
    }

    private BudgetResponse toResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .name(budget.getName())
                .periodType(budget.getPeriodType())
                .amountLimit(budget.getAmountLimit())
                .baseCurrency(budget.getBaseCurrency())
                .scopeType(budget.getScopeType())
                .categoryId(budget.getCategory() != null ? budget.getCategory().getId() : null)
                .categoryName(budget.getCategory() != null ? budget.getCategory().getName() : null)
                .region(budget.getRegion())
                .city(budget.getCity())
                .country(budget.getCountry())
                .startsAt(budget.getStartsAt())
                .endsAt(budget.getEndsAt())
                .warningThreshold(budget.getWarningThreshold())
                .isActive(budget.getIsActive())
                .partnerId(budget.getPartnerId())
                .createdAt(budget.getCreatedAt())
                .updatedAt(budget.getUpdatedAt())
                .build();
    }

    private Budget resolveBudget(Long userId, Long id) {
        List<Long> partnerIds = getAcceptedPartnerIds(userId);
        
        if (partnerIds.isEmpty()) {
            return budgetRepository.findByIdAndUserId(id, userId)
                    .orElseThrow(() -> new EntityNotFoundException("Budget not found"));
        }
        
        for (Long partnerId : partnerIds) {
            var budget = budgetRepository.findByIdAndUserId(id, partnerId);
            if (budget.isPresent() && (budget.get().getPartnerId() == null || budget.get().getPartnerId().equals(userId))) {
                return budget.get();
            }
        }
        
        return budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found"));
    }

    private List<Long> getAcceptedPartnerIds(Long userId) {
        return partnerRepository.findAllAcceptedForUser(userId).stream()
                .map(p -> p.getUserId().equals(userId) ? p.getPartnerId() : p.getUserId())
                .toList();
    }
}
