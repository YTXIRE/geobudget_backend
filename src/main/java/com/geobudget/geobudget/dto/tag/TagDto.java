package com.geobudget.geobudget.dto.tag;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDto {
    private Long id;
    private String name;
    private String color;
    private String icon;
}
