package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.geo.PlaceSuggestionDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NominatimService {
    private static final String BASE_URL = "https://nominatim.openstreetmap.org";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PlaceSuggestionDto> searchPlaces(String query, String country) {
        if (query == null || query.trim().length() < 2) {
            log.info("NominatimService: query too short");
            return new ArrayList<>();
        }

        HttpURLConnection connection = null;
        try {
            String urlStr = BASE_URL + "/search?q=" + encode(query) + 
                    "&format=json&addressdetails=1&limit=5&accept-language=ru";
            
            if (country != null && !country.isBlank()) {
                String countryCode = getCountryCode(country);
                if (!countryCode.isEmpty()) {
                    urlStr += "&countrycodes=" + countryCode;
                }
            }
            
            log.info("NominatimService: calling {}", urlStr);

            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "GeoBudgetApp/1.0 (contact@geobudget.com)");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            
            int responseCode = connection.getResponseCode();
            log.info("NominatimService: response code = {}", responseCode);
            
            String body;
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                body = br.lines().collect(Collectors.joining());
            }
            
            log.info("NominatimService: raw response length = {}", body.length());
            log.info("NominatimService: raw response = {}", body);
            
            if (body == null || body.isEmpty()) {
                log.warn("NominatimService: response body is null or empty");
                return new ArrayList<>();
            }
            
            JsonNode jsonArray = objectMapper.readTree(body);
            
            if (!jsonArray.isArray()) {
                log.warn("NominatimService: response is not an array");
                return new ArrayList<>();
            }
            
            log.info("NominatimService: got {} results", jsonArray.size());

            List<PlaceSuggestionDto> results = new ArrayList<>();
            for (JsonNode item : jsonArray) {
                results.add(mapToDto(item));
            }
            return results;
        } catch (Exception e) {
            log.error("NominatimService error: {}", e.getMessage(), e);
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private PlaceSuggestionDto mapToDto(JsonNode item) {
        JsonNode address = item.get("address");
        
        String city = null;
        String region = null;
        if (address != null && !address.isNull()) {
            city = getStringField(address, "city");
            if (city == null) city = getStringField(address, "town");
            if (city == null) city = getStringField(address, "village");
            if (city == null) city = getStringField(address, "county");
            
            region = getStringField(address, "state");
            if (region == null) region = getStringField(address, "region");
        }

        String country = (address != null && !address.isNull()) ? getStringField(address, "country") : null;
        String placeId = getStringField(item, "place_id");
        Double lat = parseDouble(getStringField(item, "lat"));
        Double lon = parseDouble(getStringField(item, "lon"));
        
        // Build custom display name: "Название" - "Город", "Область", "Страна"
        String name = getStringField(item, "name");
        if (name == null && address != null && !address.isNull()) {
            name = getStringField(address, "shop");
            if (name == null) name = getStringField(address, "amenity");
            if (name == null) name = getStringField(address, "leisure");
        }
        
        StringBuilder displayName = new StringBuilder();
        if (name != null && !name.isEmpty()) {
            displayName.append(name);
        }
        if (city != null && !city.isEmpty()) {
            if (displayName.length() > 0) displayName.append(", ");
            displayName.append(city);
        }
        if (region != null && !region.isEmpty()) {
            if (displayName.length() > 0) displayName.append(", ");
            displayName.append(region);
        }
        if (country != null && !country.isEmpty()) {
            if (displayName.length() > 0) displayName.append(", ");
            displayName.append(country);
        }
        
        String finalDisplayName = displayName.length() > 0 ? displayName.toString() : getStringField(item, "display_name");

        return new PlaceSuggestionDto(placeId, finalDisplayName, lat, lon, city, region, country);
    }
    
    private String getStringField(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        return (fieldNode != null && !fieldNode.isNull()) ? fieldNode.asText() : null;
    }

    private Double parseDouble(String value) {
        if (value == null || value.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String getCountryCode(String country) {
        if (country == null) return "";
        
        return switch (country.toLowerCase()) {
            case "россия", "russia" -> "ru";
            case "украина", "ukraine" -> "ua";
            case "беларусь", "belarus" -> "by";
            case "казахстан", "kazakhstan" -> "kz";
            case "германия", "germany" -> "de";
            case "польша", "poland" -> "pl";
            case "франция", "france" -> "fr";
            case "сша", "usa", "united states" -> "us";
            case "великобритания", "uk", "united kingdom" -> "gb";
            default -> "";
        };
    }

    private String encode(String value) {
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }
}
