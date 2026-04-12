package com.geobudget.geobudget.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCategoryDto {
    private Long id;
    private String name;
    private String transactionType;
    private String groupName;
}
