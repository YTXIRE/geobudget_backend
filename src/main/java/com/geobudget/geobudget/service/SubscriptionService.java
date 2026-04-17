package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.subscription.*;
import com.geobudget.geobudget.dto.transaction.TransactionCreateRequest;
import com.geobudget.geobudget.dto.transaction.TransactionResponse;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Subscription;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.SubscriptionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionService transactionService;

    @Transactional(readOnly = true)
    public SubscriptionsListResponse getSubscriptions(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByUserIdOrderByNextPaymentDateAscCreatedAtDesc(userId);
        List<SubscriptionResponse> items = subscriptions.stream()
                .map(this::toResponse)
                .toList();

        long activeCount = subscriptions.stream()
                .filter(subscription -> subscription.getStatus() == Subscription.Status.ACTIVE)
                .count();
        long dueSoonCount = subscriptions.stream()
                .filter(this::isDueSoon)
                .count();
        long overdueCount = subscriptions.stream()
                .filter(this::isOverdue)
                .count();
        BigDecimal monthlyProjectedTotal = subscriptions.stream()
                .filter(subscription -> subscription.getStatus() == Subscription.Status.ACTIVE)
                .map(this::toMonthlyAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return SubscriptionsListResponse.builder()
                .subscriptions(items)
                .summary(SubscriptionsListResponse.SubscriptionsSummary.builder()
                        .activeCount(activeCount)
                        .dueSoonCount(dueSoonCount)
                        .overdueCount(overdueCount)
                        .monthlyProjectedTotal(monthlyProjectedTotal)
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse getSubscription(Long id, Long userId) {
        return toResponse(getOwnedSubscription(id, userId));
    }

    @Transactional
    public SubscriptionResponse createSubscription(Long userId, CreateSubscriptionRequest request) {
        Category category = resolveExpenseCategory(userId, request.getCategoryId());
        Subscription subscription = Subscription.builder()
                .userId(userId)
                .name(request.getName().trim())
                .amount(request.getAmount())
                .currency(request.getCurrency().trim().toUpperCase())
                .billingCycle(request.getBillingCycle())
                .nextPaymentDate(request.getNextPaymentDate())
                .reminderDays(request.getReminderDays())
                .status(Subscription.Status.ACTIVE)
                .description(normalizeText(request.getDescription()))
                .color(normalizeColor(request.getColor()))
                .categoryId(category != null ? category.getId() : null)
                .build();
        return toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional
    public SubscriptionResponse updateSubscription(Long id, Long userId, UpdateSubscriptionRequest request) {
        Subscription subscription = getOwnedSubscription(id, userId);

        if (request.getName() != null && !request.getName().isBlank()) {
            subscription.setName(request.getName().trim());
        }
        if (request.getAmount() != null) {
            subscription.setAmount(request.getAmount());
        }
        if (request.getCurrency() != null && !request.getCurrency().isBlank()) {
            subscription.setCurrency(request.getCurrency().trim().toUpperCase());
        }
        if (request.getBillingCycle() != null) {
            subscription.setBillingCycle(request.getBillingCycle());
        }
        if (request.getNextPaymentDate() != null) {
            subscription.setNextPaymentDate(request.getNextPaymentDate());
        }
        if (request.getReminderDays() != null) {
            subscription.setReminderDays(request.getReminderDays());
        }
        subscription.setDescription(normalizeText(request.getDescription()));
        subscription.setColor(normalizeColor(request.getColor()));
        Category category = resolveExpenseCategory(userId, request.getCategoryId());
        subscription.setCategoryId(category != null ? category.getId() : null);
        if (request.getStatus() != null) {
            subscription.setStatus(request.getStatus());
        }

        return toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional
    public void deleteSubscription(Long id, Long userId) {
        subscriptionRepository.delete(getOwnedSubscription(id, userId));
    }

    @Transactional
    public SubscriptionResponse pauseSubscription(Long id, Long userId) {
        Subscription subscription = getOwnedSubscription(id, userId);
        subscription.setStatus(Subscription.Status.PAUSED);
        return toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional
    public SubscriptionResponse resumeSubscription(Long id, Long userId) {
        Subscription subscription = getOwnedSubscription(id, userId);
        subscription.setStatus(Subscription.Status.ACTIVE);
        return toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional
    public SubscriptionResponse cancelSubscription(Long id, Long userId) {
        Subscription subscription = getOwnedSubscription(id, userId);
        subscription.setStatus(Subscription.Status.CANCELLED);
        return toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional
    public SubscriptionResponse markPaid(Long id, Long userId, HttpServletRequest httpRequest) {
        Subscription subscription = getOwnedSubscription(id, userId);
        LocalDate today = LocalDate.now();
        Long createdTransactionId = null;

        if (subscription.getCategoryId() != null) {
            TransactionResponse createdTransaction = transactionService.create(userId, TransactionCreateRequest.builder()
                    .type("expense")
                    .amount(subscription.getAmount())
                    .categoryId(subscription.getCategoryId())
                    .description("Подписка: " + subscription.getName())
                    .occurredAt(today.atStartOfDay())
                    .originalAmount(subscription.getAmount())
                    .originalCurrency(subscription.getCurrency())
                    .build(), httpRequest);
            createdTransactionId = createdTransaction.getId();
        }

        LocalDate nextDate = subscription.getNextPaymentDate();

        do {
            nextDate = advanceDate(nextDate, subscription.getBillingCycle());
        } while (!nextDate.isAfter(today));

        subscription.setLastPaidDate(today);
        subscription.setNextPaymentDate(nextDate);
        subscription.setStatus(Subscription.Status.ACTIVE);
        return toResponse(subscriptionRepository.save(subscription), createdTransactionId);
    }

    private Subscription getOwnedSubscription(Long id, Long userId) {
        return subscriptionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
    }

    private SubscriptionResponse toResponse(Subscription subscription) {
        return toResponse(subscription, null);
    }

    private SubscriptionResponse toResponse(Subscription subscription, Long createdTransactionId) {
        LocalDate today = LocalDate.now();
        long daysUntil = ChronoUnit.DAYS.between(today, subscription.getNextPaymentDate());

        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .name(subscription.getName())
                .amount(subscription.getAmount())
                .currency(subscription.getCurrency())
                .billingCycle(subscription.getBillingCycle())
                .nextPaymentDate(subscription.getNextPaymentDate())
                .lastPaidDate(subscription.getLastPaidDate())
                .reminderDays(subscription.getReminderDays())
                .status(subscription.getStatus())
                .description(subscription.getDescription())
                .color(subscription.getColor())
                .categoryId(subscription.getCategoryId())
                .createdTransactionId(createdTransactionId)
                .overdue(isOverdue(subscription))
                .dueSoon(isDueSoon(subscription))
                .daysUntilPayment((int) daysUntil)
                .createdAt(subscription.getCreatedAt())
                .updatedAt(subscription.getUpdatedAt())
                .build();
    }

    private boolean isOverdue(Subscription subscription) {
        return subscription.getStatus() == Subscription.Status.ACTIVE
                && subscription.getNextPaymentDate().isBefore(LocalDate.now());
    }

    private boolean isDueSoon(Subscription subscription) {
        if (subscription.getStatus() != Subscription.Status.ACTIVE) {
            return false;
        }
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(subscription.getReminderDays());
        return !subscription.getNextPaymentDate().isBefore(today)
                && !subscription.getNextPaymentDate().isAfter(threshold);
    }

    private Category resolveExpenseCategory(Long userId, Long categoryId) {
        if (categoryId == null) {
            return null;
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        if (!"system".equals(category.getType()) && !userId.equals(category.getUserId())) {
            throw new EntityNotFoundException("Category not found");
        }

        if (!"expense".equals(category.getTransactionType())) {
            throw new IllegalArgumentException("Subscription category must be expense type");
        }

        return category;
    }

    private BigDecimal toMonthlyAmount(Subscription subscription) {
        return switch (subscription.getBillingCycle()) {
            case WEEKLY -> subscription.getAmount().multiply(BigDecimal.valueOf(52))
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            case MONTHLY -> subscription.getAmount();
            case QUARTERLY -> subscription.getAmount()
                    .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
            case YEARLY -> subscription.getAmount()
                    .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
        };
    }

    private LocalDate advanceDate(LocalDate date, Subscription.BillingCycle billingCycle) {
        return switch (billingCycle) {
            case WEEKLY -> date.plusWeeks(1);
            case MONTHLY -> date.plusMonths(1);
            case QUARTERLY -> date.plusMonths(3);
            case YEARLY -> date.plusYears(1);
        };
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizeColor(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim().toUpperCase();
    }
}
