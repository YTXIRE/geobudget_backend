package com.geobudget.geobudget.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagStatsResponse {

    private TagStatsSummary summary;
    private List<TagStatItem> tags;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagStatsSummary {
        private Long taggedTransactions;
        private BigDecimal taggedAmount;
        private Long uniqueTags;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagStatItem {
        private Long id;
        private String name;
        private String color;
        private String icon;
        private Long transactionsCount;
        private BigDecimal totalAmount;
    }
}
