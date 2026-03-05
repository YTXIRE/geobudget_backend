package com.geobudget.geobudget.dto.transaction;

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
public class TransactionResponse {
    private Long id;
    private String type;
    private BigDecimal amount;
    private Long categoryId;
    private String description;
    private LocalDateTime occurredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
