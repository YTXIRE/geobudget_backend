package com.geobudget.geobudget.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "savings_goals")
@EntityListeners(AuditingEntityListener.class)
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "target_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal targetAmount;

    @Column(name = "current_amount", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false, length = 3)
    @Builder.Default
    private String currency = "RUB";

    @Column(name = "icon", length = 50)
    private String icon;

    @Column(name = "color", length = 7)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type", nullable = false, length = 30)
    @Builder.Default
    private GoalType goalType = GoalType.OTHER;

    @Column(name = "priority")
    @Builder.Default
    private Integer priority = 5;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "deadline_enabled")
    @Builder.Default
    private Boolean deadlineEnabled = false;

    @Column(name = "monthly_target", precision = 15, scale = 2)
    private BigDecimal monthlyTarget;

    @Column(name = "auto_contribution_enabled")
    @Builder.Default
    private Boolean autoContributionEnabled = false;

    @Column(name = "auto_contribution_amount", precision = 15, scale = 2)
    private BigDecimal autoContributionAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "auto_contribution_source", length = 20)
    private AutoContributionSource autoContributionSource;

    @Column(name = "auto_contribution_percent", precision = 5, scale = 2)
    private BigDecimal autoContributionPercent;

    @ManyToOne
    @JoinColumn(name = "contribution_category_id")
    private Category contributionCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private GoalStatus status = GoalStatus.ACTIVE;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GoalContribution> contributions = new ArrayList<>();

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @Builder.Default
    private List<GoalMilestone> milestones = new ArrayList<>();

    public enum GoalType {
        EMERGENCY_FUND,
        VACATION,
        VEHICLE,
        HOME,
        EDUCATION,
        INVESTMENT,
        RETIREMENT,
        WEDDING,
        GIFT,
        OTHER
    }

    public enum AutoContributionSource {
        INCOME_PERCENTAGE,
        FIXED_AMOUNT,
        ROUND_UP
    }

    public enum GoalStatus {
        ACTIVE,
        COMPLETED,
        PAUSED,
        CANCELLED
    }

    public BigDecimal getProgressPercentage() {
        if (targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentAmount.multiply(BigDecimal.valueOf(100))
                .divide(targetAmount, 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal getRemainingAmount() {
        return targetAmount.subtract(currentAmount).max(BigDecimal.ZERO);
    }

    public Long getDaysRemaining() {
        if (targetDate == null || !deadlineEnabled) {
            return null;
        }
        long days = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
        return days > 0 ? days : 0L;
    }
}
