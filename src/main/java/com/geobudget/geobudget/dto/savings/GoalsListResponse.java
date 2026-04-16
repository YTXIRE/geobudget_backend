package com.geobudget.geobudget.dto.savings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalsListResponse {

    private List<SavingsGoalResponse> goals;
    private GoalsSummary summary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoalsSummary {
        private BigDecimal totalTargetAmount;
        private BigDecimal totalCurrentAmount;
        private BigDecimal overallProgress;
        private Long activeGoals;
        private Long completedGoals;
        private BigDecimal thisMonthContributions;
    }
}
