package com.geobudget.geobudget.dto.checkReceipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String name;
    private double price;
    private double quantity;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("price")
    public void setPrice(double price) {
        this.price = price / 100.0;
    }

    @JsonProperty("sum")
    public void setAmountFromSum(double sum) {
        this.amount = sum / 100.0;
    }
}


