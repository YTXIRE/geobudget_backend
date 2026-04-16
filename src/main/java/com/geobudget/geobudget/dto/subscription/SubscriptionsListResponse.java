package com.geobudget.geobudget.dto.subscription;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionsListResponse {

    private List<SubscriptionResponse> subscriptions;
    private SubscriptionsSummary summary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionsSummary {
        private Long activeCount;
        private Long dueSoonCount;
        private Long overdueCount;
        private BigDecimal monthlyProjectedTotal;
    }
}
