package com.geobudget.geobudget.dto.analytics;

import java.math.BigDecimal;
import java.util.List;

public record IncomeByPartnerResponse(
    List<PartnerIncome> partners,
    BigDecimal totalIncome
) {
    public record PartnerIncome(
        Long partnerId,
        String partnerName,
        BigDecimal amount,
        Long transactionCount,
        BigDecimal averageTransaction,
        String lastTransactionDate
    ) {}
}
