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
public class CreateSubscriptionRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotBlank
    @Size(max = 10)
    private String currency;

    @NotNull
    private Subscription.BillingCycle billingCycle;

    @NotNull
    private LocalDate nextPaymentDate;

    @NotNull
    @Min(0)
    @Max(30)
    private Integer reminderDays;

    @Size(max = 500)
    private String description;

    @Size(max = 7)
    private String color;

    private Long categoryId;
}
