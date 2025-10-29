package com.geobudget.geobudget.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "dadata")
public class DadataProperties {
    private String apiKey;
    private String secret;
    private String suggestUrl;
    private String cleanUrl;
}


