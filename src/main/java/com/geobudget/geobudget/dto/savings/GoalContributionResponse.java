package com.geobudget.geobudget.dto.savings;

import com.geobudget.geobudget.entity.GoalContribution;
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
public class GoalContributionResponse {

    private Long id;
    private Long goalId;
    private Long transactionId;
    private BigDecimal amount;
    private String currency;
    private GoalContribution.ContributionType contributionType;
    private String note;
    private LocalDate contributionDate;
    private LocalDateTime createdAt;

    public static GoalContributionResponse fromEntity(GoalContribution contribution) {
        return GoalContributionResponse.builder()
                .id(contribution.getId())
                .goalId(contribution.getGoal().getId())
                .transactionId(contribution.getTransaction() != null ? contribution.getTransaction().getId() : null)
                .amount(contribution.getAmount())
                .currency(contribution.getCurrency())
                .contributionType(contribution.getContributionType())
                .note(contribution.getNote())
                .contributionDate(contribution.getContributionDate())
                .createdAt(contribution.getCreatedAt())
                .build();
    }
}
