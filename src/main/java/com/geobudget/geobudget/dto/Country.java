package com.geobudget.geobudget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private Long id;

    @Pattern(regexp = ".+", message = "Название страны не должно быть пустым")
    @Schema(description = "Название страны", example = "Россия")
    private String title;
}
