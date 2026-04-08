package com.geobudget.geobudget.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesUpdateRequest {
    @Pattern(regexp = "[A-Z]{3}", message = "baseCurrency must be ISO-4217 (3 uppercase letters)")
    private String baseCurrency;

    @Size(max = 120, message = "homeCity size must be <= 120")
    private String homeCity;
}
