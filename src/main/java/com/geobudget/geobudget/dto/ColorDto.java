package com.geobudget.geobudget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColorDto {
    private Long id;
    private String name;
    private String hex;
    private Long argb;
}
