package com.geobudget.geobudget.exception;

import lombok.Getter;

@Getter
public class GroupHasDependenciesException extends RuntimeException {
    private final int categoryCount;
    private final int transactionCount;
    private final String categoryNames;

    public GroupHasDependenciesException(int categoryCount, int transactionCount, String categoryNames) {
        super(buildMessage(categoryCount, transactionCount, categoryNames));
        this.categoryCount = categoryCount;
        this.transactionCount = transactionCount;
        this.categoryNames = categoryNames;
    }

    private static String buildMessage(int categoryCount, int transactionCount, String categoryNames) {
        StringBuilder sb = new StringBuilder();
        sb.append("Невозможно удалить группу. ");
        
        if (categoryCount > 0) {
            sb.append("Группа содержит категории: ");
            sb.append(categoryNames);
            if (transactionCount > 0) {
                sb.append(" (всего категорий: ").append(categoryCount).append(", операций: ").append(transactionCount).append(")");
            } else {
                sb.append(" (всего категорий: ").append(categoryCount).append(")");
            }
        }
        
        return sb.toString();
    }
}
