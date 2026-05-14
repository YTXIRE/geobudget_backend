package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.PartnerDto;
import com.geobudget.geobudget.dto.PartnerInviteRequest;
import com.geobudget.geobudget.dto.PartnerStatsResponse;
import com.geobudget.geobudget.dto.budget.BudgetResponse;
import com.geobudget.geobudget.entity.Budget;
import com.geobudget.geobudget.entity.Partner;
import com.geobudget.geobudget.entity.Transaction;
import com.geobudget.geobudget.entity.User;
import com.geobudget.geobudget.repository.BudgetRepository;
import com.geobudget.geobudget.repository.PartnerRepository;
import com.geobudget.geobudget.repository.TransactionRepository;
import com.geobudget.geobudget.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    @Autowired
    private FcmService fcmService;

    @Transactional(readOnly = true)
    public List<PartnerDto> getAcceptedPartners(Long userId) {
        return partnerRepository.findAllAcceptedForUser(userId).stream()
                .map(partner -> mapToDto(partner, userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PartnerDto> getPendingInvitations(Long userId) {
        return partnerRepository.findPendingInvitations(userId).stream()
                .map(partner -> mapToDto(partner, userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PartnerDto> getPendingIncoming(Long userId) {
        return partnerRepository.findPendingIncoming(userId).stream()
                .map(partner -> mapToDto(partner, userId))
                .collect(Collectors.toList());
    }

    @Transactional
    public PartnerDto invitePartner(Long userId, PartnerInviteRequest request) {
        User partner = findUserByEmailOrPhone(request);
        
        if (partner.getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot invite yourself");
        }

        if (partnerRepository.existsAcceptedPartnership(userId, partner.getId())) {
            throw new IllegalArgumentException("Partnership already exists");
        }

        Partner existing = partnerRepository.findByUserIdAndPartnerId(userId, partner.getId())
                .orElse(null);
        
        if (existing != null) {
            if (existing.getStatus().equals(Partner.STATUS_PENDING)) {
                throw new IllegalArgumentException("Invitation already sent");
            }
            if (existing.getStatus().equals(Partner.STATUS_REJECTED)) {
                existing.setStatus(Partner.STATUS_PENDING);
                return mapToDto(partnerRepository.save(existing), userId);
            }
        }

        Partner partnerEntity = Partner.builder()
                .userId(userId)
                .partnerId(partner.getId())
                .status(Partner.STATUS_PENDING)
                .build();

        return mapToDto(partnerRepository.save(partnerEntity), userId);
    }

    @Transactional
    public PartnerDto acceptInvitation(Long userId, Long partnerId) {
        Partner invitation = partnerRepository.findByUserIdAndPartnerId(partnerId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

        if (!invitation.getStatus().equals(Partner.STATUS_PENDING)) {
            throw new IllegalArgumentException("Invitation is not pending");
        }

        if (partnerRepository.existsAcceptedPartnership(userId, partnerId)) {
            throw new IllegalArgumentException("Partnership already exists");
        }

        invitation.setStatus(Partner.STATUS_ACCEPTED);
        partnerRepository.save(invitation);
        
        notifyPartnerAccepted(userId, partnerId);
        
        return mapToDto(invitation, userId);
    }

    @Transactional
    public PartnerDto rejectInvitation(Long userId, Long partnerId) {
        Partner invitation = partnerRepository.findByUserIdAndPartnerId(partnerId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

        if (!invitation.getStatus().equals(Partner.STATUS_PENDING)) {
            throw new IllegalArgumentException("Invitation is not pending");
        }

        invitation.setStatus(Partner.STATUS_REJECTED);
        return mapToDto(partnerRepository.save(invitation), userId);
    }

    @Transactional
    public void removePartner(Long userId, Long partnerId) {
        Partner partnership = partnerRepository.findByUserIdAndPartnerId(userId, partnerId)
                .orElseGet(() -> partnerRepository.findByUserIdAndPartnerId(partnerId, userId)
                        .orElseThrow(() -> new EntityNotFoundException("Partnership not found")));

        if (!partnership.getStatus().equals(Partner.STATUS_ACCEPTED)) {
            partnership.setStatus(Partner.STATUS_REJECTED);
            partnerRepository.save(partnership);
        } else {
            partnerRepository.delete(partnership);
            partnerRepository.findByUserIdAndPartnerId(partnerId, userId)
                    .ifPresent(partnerRepository::delete);
        }
    }

    @Transactional(readOnly = true)
    public List<Long> getAcceptedPartnerIds(Long userId) {
        return partnerRepository.findAllAcceptedForUser(userId).stream()
                .map(p -> p.getUserId().equals(userId) ? p.getPartnerId() : p.getUserId())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isPartnerWith(Long userId, Long otherUserId) {
        return partnerRepository.existsAcceptedPartnership(userId, otherUserId);
    }

    @Transactional(readOnly = true)
    public PartnerStatsResponse getPartnerStats(Long userId, Long partnerId, 
            LocalDateTime from, LocalDateTime to) {
        if (!partnerRepository.existsAcceptedPartnership(userId, partnerId)) {
            return PartnerStatsResponse.builder()
                    .totalExpense(BigDecimal.ZERO)
                    .totalIncome(BigDecimal.ZERO)
                    .transactionCount(0)
                    .build();
        }

        List<Transaction> transactions = transactionRepository
                .findByUserIdAndIsDeletedFalse(partnerId);

        if (from != null) {
            transactions = transactions.stream()
                    .filter(t -> t.getOccurredAt() != null && !t.getOccurredAt().isBefore(from))
                    .collect(Collectors.toList());
        }
        if (to != null) {
            transactions = transactions.stream()
                    .filter(t -> t.getOccurredAt() != null && !t.getOccurredAt().isAfter(to))
                    .collect(Collectors.toList());
        }

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> "expense".equals(t.getType()))
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> "income".equals(t.getType()))
                .map(t -> t.getBaseAmount() != null ? t.getBaseAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Long, PartnerStatsResponse.CategoryStat> categoryMap = new LinkedHashMap<>();
        for (Transaction tx : transactions) {
            if (tx.getCategory() != null) {
                Long catId = tx.getCategory().getId();
                String catName = tx.getCategory().getName();
                BigDecimal amount = tx.getBaseAmount() != null ? tx.getBaseAmount() : tx.getAmount();
                
                categoryMap.compute(catId, (k, existing) -> {
                    if (existing == null) {
                        return PartnerStatsResponse.CategoryStat.builder()
                                .categoryId(catId)
                                .categoryName(catName)
                                .totalAmount(amount)
                                .transactionCount(1)
                                .build();
                    } else {
                        return PartnerStatsResponse.CategoryStat.builder()
                                .categoryId(existing.getCategoryId())
                                .categoryName(existing.getCategoryName())
                                .totalAmount(existing.getTotalAmount().add(amount))
                                .transactionCount(existing.getTransactionCount() + 1)
                                .build();
                    }
                });
            }
        }

        return PartnerStatsResponse.builder()
                .totalExpense(totalExpense)
                .totalIncome(totalIncome)
                .transactionCount(transactions.size())
                .byCategory(new ArrayList<>(categoryMap.values()))
                .build();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSharedBudgets(Long userId) {
        List<Long> partnerIds = getAcceptedPartnerIds(userId);
        List<BudgetResponse> shared = new ArrayList<>();

        for (Long partnerId : partnerIds) {
            List<Budget> budgets = budgetRepository.findByUserIdOrderByCreatedAtDesc(partnerId);
            for (Budget b : budgets) {
                if (b.getPartnerId() != null && b.getPartnerId().equals(userId)) {
                    shared.add(toBudgetResponse(b));
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("sharedBudgets", shared);
        result.put("count", shared.size());
        return result;
    }

    private BudgetResponse toBudgetResponse(Budget budget) {
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
                .startsAt(budget.getStartsAt())
                .endsAt(budget.getEndsAt())
                .warningThreshold(budget.getWarningThreshold())
                .isActive(budget.getIsActive())
                .partnerId(budget.getPartnerId())
                .createdAt(budget.getCreatedAt())
                .updatedAt(budget.getUpdatedAt())
                .build();
    }

    private User findUserByEmailOrPhone(PartnerInviteRequest request) {
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            return userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with this email"));
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            return userRepository.findByPhone(request.getPhone().trim())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with this phone"));
        }
        throw new IllegalArgumentException("Email or phone is required");
    }

    private PartnerDto mapToDto(Partner partner, Long currentUserId) {
        Long counterpartUserId = Objects.equals(partner.getUserId(), currentUserId)
                ? partner.getPartnerId()
                : partner.getUserId();

        User partnerUser = userRepository.findById(counterpartUserId).orElse(null);

        return PartnerDto.builder()
                .id(partner.getId())
                .userId(partner.getUserId())
                .partnerId(counterpartUserId)
                .partnerUsername(partnerUser != null ? partnerUser.getUsername() : null)
                .partnerEmail(partnerUser != null ? partnerUser.getEmail() : null)
                .partnerPhone(partnerUser != null ? partnerUser.getPhone() : null)
                .status(partner.getStatus())
                .createdAt(partner.getCreatedAt())
                .updatedAt(partner.getUpdatedAt())
                .build();
    }

    private void notifyPartnerAccepted(Long userId, Long partnerId) {
        if (fcmService == null) return;
        
        User user = userRepository.findById(userId).orElse(null);
        String userName = user != null ? user.getUsername() : "Пользователь";
        
        fcmService.sendNotification(
            partnerId,
            "Новый партнёр",
            "$userName принял ваш запрос в партнёры"
        );
    }

    private void notifyLargeExpense(Long userId, Long partnerId, BigDecimal amount) {
        if (fcmService == null) return;
        
        User user = userRepository.findById(userId).orElse(null);
        String userName = user != null ? user.getUsername() : "Пользователь";
        
        fcmService.sendNotification(
            partnerId,
            "Крупная трата партнёра",
            "$userName потратил ${amount.setScale(0, java.math.RoundingMode.HALF_UP)}"
        );
    }
}
