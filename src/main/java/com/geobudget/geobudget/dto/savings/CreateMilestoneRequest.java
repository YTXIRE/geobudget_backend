package com.geobudget.geobudget.dto.savings;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreateMilestoneRequest {

    @NotBlank(message = "Название вехи обязательно")
    @Size(max = 100, message = "Название не может быть длиннее 100 символов")
    private String name;

    @NotNull(message = "Целевая сумма обязательна")
    @DecimalMin(value = "1.00", message = "Целевая сумма должна быть больше 0")
    private BigDecimal targetAmount;

    private LocalDate targetDate;

    private Integer orderIndex;
}
