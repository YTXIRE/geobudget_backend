package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.geoCompany.CountryAndCity;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.geobudget.geobudget.validator.GeoValidator;

import java.net.InetAddress;

@Service
public class GeoIpService {
    private static final String GEO_IP_URL = "http://ip-api.com/json/{ip}?lang=ru";
    private static final String PUBLIC_IP_URL = "https://api4.ipify.org?format=json";
    private static final Logger log = LoggerFactory.getLogger(GeoIpService.class);

    private final RestTemplate restTemplate;
    private final GeoValidator geoValidator;

    public GeoIpService(RestTemplate restTemplate, GeoValidator geoValidator) {
        this.restTemplate = restTemplate;
        this.geoValidator = geoValidator;
    }

    public CountryAndCity getCityAndCountryByIp(String ip) {
        try {
            geoValidator.validateIp(ip);
            String response = restTemplate.getForObject(GEO_IP_URL, String.class, ip);
            JSONObject json = new JSONObject(response);
            String city = json.optString("city", null);
            String country = json.optString("country", null);
            Double latitude = json.has("lat") && !json.isNull("lat") ? json.getDouble("lat") : null;
            Double longitude = json.has("lon") && !json.isNull("lon") ? json.getDouble("lon") : null;
            log.info("GeoIpService.getCityByIp: ip={} city={} lat={} lon={}", ip, city, latitude, longitude);
            CountryAndCity data = new CountryAndCity();
            data.setCountry(country);
            data.setCity(city);
            data.setLatitude(latitude);
            data.setLongitude(longitude);
            return data;
        } catch (Exception e) {
            log.error("GeoIpService.getCityByIp: failed for ip={}", ip, e);
            return null;
        }
    }

    public String getExternalIp(HttpServletRequest request) {
        String ip = extractHeaderIp(request.getHeader("X-Forwarded-For"));
        if (ip != null && !isLocalOrPrivateIp(ip)) {
            return ip;
        }

        ip = extractHeaderIp(request.getHeader("X-Real-IP"));
        if (ip != null && !isLocalOrPrivateIp(ip)) {
            return ip;
        }

        ip = request.getRemoteAddr();

        if (isLocalOrPrivateIp(ip)) {
            String publicIp = resolvePublicIp();
            if (publicIp != null) {
                return publicIp;
            }
        }

        return ip;
    }

    public CountryAndCity getCityByExternalIp(HttpServletRequest request) {
        String ip = getExternalIp(request);
        log.info("GeoIpService.getCityByExternalIp: ip={}", ip);
        return getCityAndCountryByIp(ip);
    }

    private String extractHeaderIp(String headerValue) {
        if (headerValue == null || headerValue.isBlank() || "unknown".equalsIgnoreCase(headerValue)) {
            return null;
        }

        String candidate = headerValue.split(",")[0].trim();
        return candidate.isEmpty() ? null : candidate;
    }

    private String resolvePublicIp() {
        try {
            String response = restTemplate.getForObject(PUBLIC_IP_URL, String.class);
            if (response == null || response.isBlank()) {
                return null;
            }

            JSONObject json = new JSONObject(response);
            String ip = json.optString("ip", null);
            if (ip == null || ip.isBlank()) {
                return null;
            }

            geoValidator.validateIp(ip);
            log.info("GeoIpService.resolvePublicIp: resolved public ip={}", ip);
            return ip;
        } catch (Exception e) {
            log.warn("GeoIpService.resolvePublicIp: failed to resolve public ip", e);
            return null;
        }
    }

    private boolean isLocalOrPrivateIp(String ip) {
        if (ip == null || ip.isBlank()) {
            return true;
        }

        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isAnyLocalAddress()
                    || address.isLoopbackAddress()
                    || address.isSiteLocalAddress()
                    || address.isLinkLocalAddress();
        } catch (Exception e) {
            log.debug("GeoIpService.isLocalOrPrivateIp: failed to parse ip={}", ip, e);
            return true;
        }
    }
}
