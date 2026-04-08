package com.geobudget.geobudget.dto.geoCompany;

public class CountryAndCity {
    private String country;
    private String city;
    private String region;
    private Double latitude;
    private Double longitude;

    public CountryAndCity() {
    }

    public CountryAndCity(String country, String city, Double latitude, Double longitude) {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CountryAndCity(String country, String city, String region, Double latitude, Double longitude) {
        this.country = country;
        this.city = city;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
