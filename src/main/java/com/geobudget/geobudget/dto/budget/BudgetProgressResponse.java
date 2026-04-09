package com.geobudget.geobudget.dto.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetProgressResponse {
    private Long budgetId;
    private String name;
    private String scopeType;
    private String scopeLabel;
    private BigDecimal amountLimit;
    private BigDecimal spent;
    private BigDecimal remaining;
    private BigDecimal progressRatio;
    private String status;
    private Long matchedTransactionsCount;
    private String baseCurrency;
    private List<BudgetDailyPointResponse> dailySeries;
    private BigDecimal averageDailySpend;
    private BigDecimal safeDailySpend;
    private BigDecimal projectedSpent;
    private LocalDate projectedExceedDate;
    private List<BudgetTopTransactionResponse> topTransactions;
}
