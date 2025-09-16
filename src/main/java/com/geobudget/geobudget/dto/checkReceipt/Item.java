package com.geobudget.geobudget.dto.checkReceipt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String name;
    private double price;
    private int quantity;

    @JsonProperty("price")
    public void setPrice(double price) {
        this.price = price / 100.0;
    }
}

