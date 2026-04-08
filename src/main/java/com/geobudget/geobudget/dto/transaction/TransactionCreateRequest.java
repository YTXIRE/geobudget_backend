package com.geobudget.geobudget.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class TransactionCreateRequest {
    @NotBlank(message = "type is required")
    @Pattern(regexp = "income|expense", message = "type must be income or expense")
    private String type;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.01", message = "amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "categoryId is required")
    private Long categoryId;

    @Size(max = 255, message = "description size must be <= 255")
    private String description;

    @NotNull(message = "occurredAt is required")
    private LocalDateTime occurredAt;

    @DecimalMin(value = "-90.0", message = "latitude must be >= -90")
    @DecimalMax(value = "90.0", message = "latitude must be <= 90")
    private BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "longitude must be >= -180")
    @DecimalMax(value = "180.0", message = "longitude must be <= 180")
    private BigDecimal longitude;

    @Size(max = 120, message = "city size must be <= 120")
    private String city;

    @Size(max = 120, message = "country size must be <= 120")
    private String country;

    @Size(max = 160, message = "region size must be <= 160")
    private String region;

    @Size(max = 255, message = "placeId size must be <= 255")
    private String placeId;

    @Pattern(regexp = "gps|manual|map|ip", message = "locationSource must be gps, manual, map or ip")
    private String locationSource;

    @DecimalMin(value = "0.01", message = "originalAmount must be greater than 0")
    private BigDecimal originalAmount;

    @Pattern(regexp = "[A-Z]{3}", message = "originalCurrency must be ISO-4217 (3 uppercase letters)")
    private String originalCurrency;

    @DecimalMin(value = "0.000001", message = "rateToBase must be greater than 0")
    private BigDecimal rateToBase;

    @DecimalMin(value = "0.01", message = "baseAmount must be greater than 0")
    private BigDecimal baseAmount;
}
