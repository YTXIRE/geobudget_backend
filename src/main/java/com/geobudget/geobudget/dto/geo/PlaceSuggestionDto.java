package com.geobudget.geobudget.dto.geo;

public class PlaceSuggestionDto {
    public String placeId;
    public String displayName;
    public Double latitude;
    public Double longitude;
    public String city;
    public String region;
    public String country;

    public PlaceSuggestionDto() {}

    public PlaceSuggestionDto(String placeId, String displayName, Double latitude, 
            Double longitude, String city, String region, String country) {
        this.placeId = placeId;
        this.displayName = displayName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.region = region;
        this.country = country;
    }
}
