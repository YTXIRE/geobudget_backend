package com.geobudget.geobudget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    @Schema(description = "Код валюты (ISO 4217)", example = "RUB")
    private String code;

    @Schema(description = "Название валюты на русском", example = "Российский рубль")
    private String name;

    @Schema(description = "Символ валюты", example = "₽")
    private String symbol;
}
