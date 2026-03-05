package com.geobudget.geobudget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private Long id;
    private String name;
    private Long userId;
    private Boolean isSystem;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
