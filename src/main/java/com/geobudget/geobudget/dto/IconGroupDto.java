package com.geobudget.geobudget.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IconGroupDto {
    private Long id;
    private String name;
    private List<IconDto> icons;
}
