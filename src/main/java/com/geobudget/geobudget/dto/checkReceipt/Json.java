package com.geobudget.geobudget.dto.checkReceipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Json {
    private String user;
    private List<Item> items;
    private String dateTime;
    private double totalSum;
    private String region;
    private String userInn;
    private String retailPlaceAddress;
}

