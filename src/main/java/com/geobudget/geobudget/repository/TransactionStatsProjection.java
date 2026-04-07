package com.geobudget.geobudget.repository;

import java.math.BigDecimal;

public interface TransactionStatsProjection {
    BigDecimal getTotalAmount();
    Long getCount();
}
