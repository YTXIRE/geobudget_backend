package com.geobudget.geobudget.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "receipts.retry")
public class ReceiptsRetryProperties {
    private int delay;
    private int attempts;
}
