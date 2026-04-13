package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.TransactionTemplateDto;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.TransactionTemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/templates")
@PreAuthorize("isAuthenticated()")
public class TransactionTemplateController {
    
    private final TransactionTemplateService templateService;

    public TransactionTemplateController(TransactionTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionTemplateDto>> getTemplates(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(templateService.getTemplates(userDetails.getUserId()));
    }

    @PostMapping
    public ResponseEntity<TransactionTemplateDto> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, Object> body) {
        
        String name = (String) body.get("name");
        String type = (String) body.get("type");
        Long categoryId = body.get("categoryId") != null 
                ? ((Number) body.get("categoryId")).longValue() : null;
        BigDecimal amount = body.get("amount") != null 
                ? new BigDecimal(body.get("amount").toString()) : null;
        String currency = (String) body.get("currency");
        String description = (String) body.get("description");
        Boolean recurrenceEnabled = body.get("recurrenceEnabled") != null 
                ? (Boolean) body.get("recurrenceEnabled") : false;
        String recurrenceType = (String) body.get("recurrenceType");
        String recurrenceDays = (String) body.get("recurrenceDays");
        Integer dayOfMonth = body.get("dayOfMonth") != null 
                ? ((Number) body.get("dayOfMonth")).intValue() : null;
        String city = (String) body.get("city");
        String country = (String) body.get("country");
        String region = (String) body.get("region");
        Double latitude = body.get("latitude") != null 
                ? ((Number) body.get("latitude")).doubleValue() : null;
        Double longitude = body.get("longitude") != null 
                ? ((Number) body.get("longitude")).doubleValue() : null;
        String placeId = (String) body.get("placeId");
        String locationSource = (String) body.get("locationSource");

        TransactionTemplateDto dto = templateService.create(
                userDetails.getUserId(), name, type, categoryId, amount, currency, description,
                recurrenceEnabled, recurrenceType, recurrenceDays, dayOfMonth,
                city, country, region, latitude, longitude, placeId, locationSource);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionTemplateDto> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        
        String name = (String) body.get("name");
        String type = (String) body.get("type");
        Long categoryId = body.get("categoryId") != null 
                ? ((Number) body.get("categoryId")).longValue() : null;
        BigDecimal amount = body.get("amount") != null 
                ? new BigDecimal(body.get("amount").toString()) : null;
        String currency = (String) body.get("currency");
        String description = (String) body.get("description");
        Boolean recurrenceEnabled = body.get("recurrenceEnabled") != null 
                ? (Boolean) body.get("recurrenceEnabled") : false;
        String recurrenceType = (String) body.get("recurrenceType");
        String recurrenceDays = (String) body.get("recurrenceDays");
        Integer dayOfMonth = body.get("dayOfMonth") != null 
                ? ((Number) body.get("dayOfMonth")).intValue() : null;
        String city = (String) body.get("city");
        String country = (String) body.get("country");
        String region = (String) body.get("region");
        Double latitude = body.get("latitude") != null 
                ? ((Number) body.get("latitude")).doubleValue() : null;
        Double longitude = body.get("longitude") != null 
                ? ((Number) body.get("longitude")).doubleValue() : null;
        String placeId = (String) body.get("placeId");
        String locationSource = (String) body.get("locationSource");

        TransactionTemplateDto dto = templateService.update(
                userDetails.getUserId(), id, name, type, categoryId, amount, currency, description,
                recurrenceEnabled, recurrenceType, recurrenceDays, dayOfMonth,
                city, country, region, latitude, longitude, placeId, locationSource);
        
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        templateService.delete(userDetails.getUserId(), id);
        return ResponseEntity.noContent().build();
    }
}
