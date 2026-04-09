package com.geobudget.geobudget.dto.budget;

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
public class BudgetTopTransactionResponse {
    private Long transactionId;
    private String categoryName;
    private String description;
    private BigDecimal amount;
    private LocalDateTime occurredAt;
}
