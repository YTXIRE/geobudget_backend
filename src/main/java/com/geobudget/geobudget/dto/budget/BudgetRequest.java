package com.geobudget.geobudget.dto.budget;

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
public class BudgetRequest {
    @NotBlank
    @Size(max = 120)
    private String name;

    @Pattern(regexp = "monthly|weekly|custom")
    private String periodType;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amountLimit;

    @Pattern(regexp = "[A-Z]{3}")
    private String baseCurrency;

    @Pattern(regexp = "category|region|city")
    private String scopeType;

    private Long categoryId;

    @Size(max = 160)
    private String region;

    @Size(max = 120)
    private String city;

    @NotNull
    private LocalDate startsAt;

    @NotNull
    private LocalDate endsAt;

    @NotNull
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "1.00")
    private BigDecimal warningThreshold;

    private Boolean isActive;
}
