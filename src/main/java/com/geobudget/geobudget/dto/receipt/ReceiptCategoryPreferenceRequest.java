package com.geobudget.geobudget.dto.receipt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceiptCategoryPreferenceRequest(
        @NotBlank String inn,
        @NotNull Long categoryId
) {
}
