package com.geobudget.geobudget.dto.tag;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTagRequest {
    
    @Size(max = 50, message = "Название тега не более 50 символов")
    private String name;
    
    @Size(max = 7, message = "Цвет не более 7 символов")
    private String color;
    
    @Size(max = 50, message = "Иконка не более 50 символов")
    private String icon;
}
