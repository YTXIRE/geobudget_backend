package com.geobudget.geobudget.service;

import com.geobudget.geobudget.config.ReceiptsProperties;
import com.geobudget.geobudget.dto.checkReceipt.CheckReceipt;
import com.geobudget.geobudget.validator.ReceiptValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckReceiptService {
    private final RestTemplate restTemplate;
    private final ReceiptValidator receiptValidator;
    private final ReceiptsProperties receiptsProperties;

    @Retryable(
            backoff = @Backoff(delay = 100)
    )
    public CheckReceipt checkReceipt(String qrString) {
        receiptValidator.validateQr(qrString);
        String url = "https://proverkacheka.com/api/v1/check/get";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("qrraw", qrString);
        requestBody.put("token", receiptsProperties.getApiKey());
        requestBody.put("qr", "3");

        try {
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<CheckReceipt> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    CheckReceipt.class
            );
            CheckReceipt body = response.getBody();
            log.info("CheckReceiptService.checkReceipt: status={} hasBody={} ", response.getStatusCode(), body != null);
            return body;
        } catch (HttpMessageNotReadableException e) {
            log.error("CheckReceiptService.checkReceipt: unreadable response", e);
            throw e;
        } catch (Exception e) {
            log.error("CheckReceiptService.checkReceipt: request failed", e);
            throw e;
        }
    }
}
