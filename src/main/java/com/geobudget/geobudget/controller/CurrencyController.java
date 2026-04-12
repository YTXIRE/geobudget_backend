package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.CurrencyDto;
import com.geobudget.geobudget.service.CurrencyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
@Tag(name = "Currency", description = "Работа с валютами")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getCurrencies() {
        return ResponseEntity.ok(currencyService.getCurrencies());
    }
}
