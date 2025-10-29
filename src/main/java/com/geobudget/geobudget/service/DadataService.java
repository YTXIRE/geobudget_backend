package com.geobudget.geobudget.service;

import com.geobudget.geobudget.config.DadataProperties;
import com.geobudget.geobudget.dto.companyInfo.CompanyInfo;
import com.geobudget.geobudget.dto.geoCompany.AddressDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DadataService {
    private final RestTemplate restTemplate;
    private final DadataProperties dadataProperties;

    @Retryable(
            backoff = @Backoff(delay = 100)
    )
    public CompanyInfo getCompanyByInn(String inn) throws Exception {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", inn);
        requestBody.put("count", "1");

        HttpHeaders headers = createHeaders();

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<CompanyInfo> response = restTemplate.exchange(
                dadataProperties.getSuggestUrl(),
                HttpMethod.POST,
                entity,
                CompanyInfo.class
        );

        CompanyInfo body = response.getBody();
        int suggestions = body != null && body.getSuggestions() != null ? body.getSuggestions().size() : 0;
        log.info("DadataService.getCompanyByInn: inn={} suggestions={} status={}", inn, suggestions, response.getStatusCode());
        return body;
    }

    @Retryable(
            backoff = @Backoff(delay = 100)
    )
    public AddressDto getGeoCompany(String address) throws Exception {
        HttpHeaders headers = createHeaders();

        String[] requestBody = {address};

        HttpEntity<String[]> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<AddressDto[]> response = restTemplate.exchange(
                dadataProperties.getCleanUrl(),
                HttpMethod.POST,
                entity,
                AddressDto[].class
        );

        AddressDto[] body = response.getBody();
        if (body != null && body.length > 0) {
            log.info("DadataService.getGeoCompany: address found status={}", response.getStatusCode());
            return body[0];
        } else {
            log.warn("DadataService.getGeoCompany: address not found status={}", response.getStatusCode());
            throw new Exception("Адрес не найден");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + dadataProperties.getApiKey());
        headers.set("X-Secret", dadataProperties.getSecret());
        return headers;
    }
}
