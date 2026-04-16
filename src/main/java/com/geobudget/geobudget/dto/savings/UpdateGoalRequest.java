package com.geobudget.geobudget.dto.savings;

import com.geobudget.geobudget.entity.SavingsGoal;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGoalRequest {

    @Size(max = 100, message = "Название не может быть длиннее 100 символов")
    private String name;

    @Size(max = 255, message = "Описание не может быть длиннее 255 символов")
    private String description;

    @DecimalMin(value = "1.00", message = "Целевая сумма должна быть больше 0")
    private BigDecimal targetAmount;

    private String currency;

    private String icon;

    private String color;

    private SavingsGoal.GoalType goalType;

    @Min(value = 1, message = "Приоритет должен быть от 1 до 10")
    @Max(value = 10, message = "Приоритет должен быть от 1 до 10")
    private Integer priority;

    private LocalDate targetDate;

    private Boolean deadlineEnabled;

    private BigDecimal monthlyTarget;

    private Boolean autoContributionEnabled;

    private BigDecimal autoContributionAmount;

    private SavingsGoal.AutoContributionSource autoContributionSource;

    private BigDecimal autoContributionPercent;

    private Long contributionCategoryId;
}
