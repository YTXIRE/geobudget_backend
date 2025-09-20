package com.geobudget.geobudget.dto.checkReceipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // игнорируем code, first, request
public class CheckReceipt {
    private List<Item> items;
    private String companyName;
    private String timeOfPurchase;
    private double totalSum;
    private String region;
    private String inn;
    private String shopAddress;

    @JsonProperty("data")
    public void setData(DataJson data) {
        if (data != null && data.getJson() != null) {
            Json json = data.getJson();
            this.items = json.getItems();
            this.companyName = json.getUser();
            this.region = json.getRegion();
            this.inn = json.getUserInn() != null ? json.getUserInn().strip() : null;
            this.shopAddress = json.getRetailPlaceAddress();
            this.timeOfPurchase = json.getDateTime();
            this.totalSum = json.getTotalSum() / 100.0; // API присылает сумму в копейках
        }
    }
}
