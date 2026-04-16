package com.geobudget.geobudget.dto.subscription;

import com.geobudget.geobudget.entity.Subscription;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {

    private Long id;
    private String name;
    private BigDecimal amount;
    private String currency;
    private Subscription.BillingCycle billingCycle;
    private LocalDate nextPaymentDate;
    private LocalDate lastPaidDate;
    private Integer reminderDays;
    private Subscription.Status status;
    private String description;
    private String color;
    private Long categoryId;
    private Long createdTransactionId;
    private Boolean overdue;
    private Boolean dueSoon;
    private Integer daysUntilPayment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
