package com.geobudget.geobudget.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryResponse {
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal balance;
}
