package com.geobudget.geobudget.dto.subscription;

import com.geobudget.geobudget.entity.Subscription;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubscriptionRequest {

    @Size(max = 120)
    private String name;

    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @Size(max = 10)
    private String currency;

    private Subscription.BillingCycle billingCycle;

    private LocalDate nextPaymentDate;

    @Min(0)
    @Max(30)
    private Integer reminderDays;

    @Size(max = 500)
    private String description;

    @Size(max = 7)
    private String color;

    private Long categoryId;

    private Subscription.Status status;
}
