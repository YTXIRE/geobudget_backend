package com.geobudget.geobudget.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUpdateRequest {
    @NotBlank(message = "type is required")
    @Pattern(regexp = "income|expense", message = "type must be income or expense")
    private String type;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "categoryId is required")
    private Long categoryId;

    @Size(max = 255, message = "description size must be <= 255")
    private String description;

    @NotNull(message = "occurredAt is required")
    private LocalDateTime occurredAt;
}
