package com.geobudget.geobudget.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "category")
public class CategoryProperties {
    private Integer fallbackId = 11;
}


