package com.geobudget.geobudget.repository;

import java.math.BigDecimal;

public interface TransactionSummaryProjection {
    BigDecimal getIncome();
    BigDecimal getExpense();
}
