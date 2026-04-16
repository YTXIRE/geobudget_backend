package com.geobudget.geobudget.dto.savings;

import com.geobudget.geobudget.entity.GoalMilestone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalMilestoneResponse {

    private Long id;
    private Long goalId;
    private String name;
    private BigDecimal targetAmount;
    private LocalDate targetDate;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
    private Integer orderIndex;
    private BigDecimal progressPercentage;

    public static GoalMilestoneResponse fromEntity(GoalMilestone milestone, BigDecimal currentGoalAmount) {
        return GoalMilestoneResponse.builder()
                .id(milestone.getId())
                .goalId(milestone.getGoal().getId())
                .name(milestone.getName())
                .targetAmount(milestone.getTargetAmount())
                .targetDate(milestone.getTargetDate())
                .isCompleted(milestone.getIsCompleted())
                .completedAt(milestone.getCompletedAt())
                .orderIndex(milestone.getOrderIndex())
                .progressPercentage(milestone.getProgressPercentage(currentGoalAmount))
                .build();
    }
}
