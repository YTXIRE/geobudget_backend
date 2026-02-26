package com.geobudget.geobudget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IconDto {
    private Long id;
    private String name;
    private Long codePoint;
}
