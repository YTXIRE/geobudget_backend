package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.fx.FxRateResponse;
import com.geobudget.geobudget.service.FxRateService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/fx")
@PreAuthorize("isAuthenticated()")
public class FxController {
    private final FxRateService fxRateService;

    public FxController(FxRateService fxRateService) {
        this.fxRateService = fxRateService;
    }

    @GetMapping("/rate")
    public ResponseEntity<FxRateResponse> getRate(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String normalizedFrom = fxRateService.normalizeCurrency(from);
        String normalizedTo = fxRateService.normalizeCurrency(to);

        return ResponseEntity.ok(fxRateService.getRate(normalizedFrom, normalizedTo, date));
    }
}
