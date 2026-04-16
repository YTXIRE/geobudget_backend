package com.geobudget.geobudget.dto.savings;

import com.geobudget.geobudget.entity.GoalMilestone;
import com.geobudget.geobudget.entity.SavingsGoal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingsGoalResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private String currency;
    private String icon;
    private String color;
    private SavingsGoal.GoalType goalType;
    private Integer priority;
    private LocalDate targetDate;
    private Boolean deadlineEnabled;
    private BigDecimal monthlyTarget;
    private Boolean autoContributionEnabled;
    private SavingsGoal.AutoContributionSource autoContributionSource;
    private BigDecimal autoContributionPercent;
    private Long contributionCategoryId;
    private SavingsGoal.GoalStatus status;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private BigDecimal progressPercentage;
    private BigDecimal remainingAmount;
    private Long daysRemaining;
    private Boolean isOnTrack;
    private MilestoneResponse nextMilestone;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MilestoneResponse {
        private Long id;
        private String name;
        private BigDecimal targetAmount;
        private Boolean isCompleted;
        private BigDecimal progressPercentage;
    }

    public static SavingsGoalResponse fromEntity(SavingsGoal goal) {
        return fromEntity(goal, null);
    }

    public static SavingsGoalResponse fromEntity(SavingsGoal goal, GoalMilestone nextMilestone) {
        SavingsGoalResponseBuilder builder = SavingsGoalResponse.builder()
                .id(goal.getId())
                .name(goal.getName())
                .description(goal.getDescription())
                .targetAmount(goal.getTargetAmount())
                .currentAmount(goal.getCurrentAmount())
                .currency(goal.getCurrency())
                .icon(goal.getIcon())
                .color(goal.getColor())
                .goalType(goal.getGoalType())
                .priority(goal.getPriority())
                .targetDate(goal.getTargetDate())
                .deadlineEnabled(goal.getDeadlineEnabled())
                .monthlyTarget(goal.getMonthlyTarget())
                .autoContributionEnabled(goal.getAutoContributionEnabled())
                .autoContributionSource(goal.getAutoContributionSource())
                .autoContributionPercent(goal.getAutoContributionPercent())
                .contributionCategoryId(goal.getContributionCategory() != null ? goal.getContributionCategory().getId() : null)
                .status(goal.getStatus())
                .completedAt(goal.getCompletedAt())
                .createdAt(goal.getCreatedAt())
                .updatedAt(goal.getUpdatedAt())
                .progressPercentage(goal.getProgressPercentage())
                .remainingAmount(goal.getRemainingAmount())
                .daysRemaining(goal.getDaysRemaining());

        if (goal.getMonthlyTarget() != null && goal.getDaysRemaining() != null && goal.getDaysRemaining() > 0) {
            BigDecimal monthlySaved = goal.getCurrentAmount()
                    .divide(BigDecimal.valueOf(java.time.temporal.ChronoUnit.MONTHS.between(
                            goal.getCreatedAt().toLocalDate(), LocalDate.now())), 2, java.math.RoundingMode.HALF_UP);
            builder.isOnTrack(monthlySaved.compareTo(goal.getMonthlyTarget()) >= 0);
        } else {
            builder.isOnTrack(true);
        }

        if (nextMilestone != null) {
            builder.nextMilestone(MilestoneResponse.builder()
                    .id(nextMilestone.getId())
                    .name(nextMilestone.getName())
                    .targetAmount(nextMilestone.getTargetAmount())
                    .isCompleted(nextMilestone.getIsCompleted())
                    .progressPercentage(nextMilestone.getProgressPercentage(goal.getCurrentAmount()))
                    .build());
        }

        return builder.build();
    }
}
