package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.geoCompany.CountryAndCity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.geobudget.geobudget.validator.GeoValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoIpService {
    private final RestTemplate restTemplate;
    private final GeoValidator geoValidator;

    public CountryAndCity getCityAndCountryByIp(String ip) {
        try {
            geoValidator.validateIp(ip);
            String response = restTemplate.getForObject("http://ip-api.com/json/{ip}?lang=ru", String.class, ip);
            JSONObject json = new JSONObject(response);
            String city = json.optString("city", null);
            String country = json.optString("country", null);
            log.info("GeoIpService.getCityByIp: ip={} city={}", ip, city);
            return new CountryAndCity(country, city);
        } catch (Exception e) {
            log.error("GeoIpService.getCityByIp: failed for ip={}", ip, e);
            return null;
        }
    }

    public String getExternalIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }

        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getRemoteAddr();

        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    public CountryAndCity getCityByExternalIp(HttpServletRequest request) {
        String ip = getExternalIp(request);
        log.info("GeoIpService.getCityByExternalIp: ip={}", ip);
        return getCityAndCountryByIp(ip);
    }
}
