package com.geobudget.geobudget.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geobudget.geobudget.config.ReceiptsProperties;
import com.geobudget.geobudget.dto.CategoryDto;
import com.geobudget.geobudget.dto.checkReceipt.CheckReceipt;
import com.geobudget.geobudget.validator.ReceiptValidator;
import jakarta.transaction.Transactional;
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
import org.springframework.web.client.RestClientException;
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

    private final CategoryService categoryService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Retryable(
            backoff = @Backoff(delay = 100),
            value = {RestClientException.class},
            maxAttempts = 3
    )
    @Transactional
    public CheckReceipt checkReceipt(String qrString) throws Exception {
        log.info("CheckReceiptService.checkReceipt: qrString={}", qrString);
        receiptValidator.validateQr(qrString.trim());

        String url = "https://proverkacheka.com/api/v1/check/get";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("qrraw", qrString);
        requestBody.put("token", receiptsProperties.getApiKey());
        requestBody.put("qr", "3");

        for (int i = 1; i <= 10; i++) {
            log.info("CheckReceiptService.checkReceipt: attempt {}", i + 1);
            try {
                HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

                String body = response.getBody();
                if (body != null && body.contains("Не авторизован")) {
                    log.warn("CheckReceiptService.checkReceipt: body={}", body);
                    Thread.sleep(300 * i);
                    continue;
                }

                log.info("CheckReceiptService.checkReceipt: status={} hasBody={}", response.getStatusCode(), body != null);

                if (body != null) {
                    log.info("CheckReceiptService.checkReceipt: body={}", body);

                    CheckReceipt dataJson = objectMapper.readValue(body, CheckReceipt.class);
                    log.info("CheckReceiptService.checkReceipt: dataJson={}", dataJson);

                    CategoryDto categoryDto = categoryService.getCategory(dataJson.getInn());
                    log.info("CheckReceiptService.checkReceipt: companyInfo={}", categoryDto);

                    dataJson.setCategory(categoryDto);

                    return dataJson;
                }
            } catch (HttpMessageNotReadableException e) {
                log.error("CheckReceiptService.checkReceipt: unreadable response", e);
                throw e;
            } catch (Exception e) {
                log.error("CheckReceiptService.checkReceipt: request failed", e);
                throw e;
            }
        }
        return null;
    }
}
