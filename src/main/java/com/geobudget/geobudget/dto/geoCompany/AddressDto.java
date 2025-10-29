package com.geobudget.geobudget.dto.geoCompany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto {
    @JsonProperty("geo_lat")
    private String geoLat;

    @JsonProperty("geo_lon")
    private String geoLon;

    private String mapUrl;

    public String getMapUrl() {
        if (geoLat != null && geoLon != null) {
            return String.format("https://www.google.com/maps/search/?api=1&query=%s,%s", geoLat, geoLon);
        }
        return null;
    }
}
