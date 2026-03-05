package com.geobudget.geobudget.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private IconDto icon;
    private ColorDto color;
    private Boolean isFavorite;
    private Boolean isArchived;
    private Long groupId;
    private CategoryGroupDto group;
    private String type;
    @NotBlank(message = "transactionType is required")
    @Pattern(regexp = "income|expense", message = "transactionType must be income or expense")
    private String transactionType;
    private Long userId;
    private Integer transactionCount;
    private BigDecimal totalSum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
