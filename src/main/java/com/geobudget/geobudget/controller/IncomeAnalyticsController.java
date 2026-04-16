package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.analytics.*;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.IncomeAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics/income")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class IncomeAnalyticsController {

    private final IncomeAnalyticsService analyticsService;

    @GetMapping("/summary")
    public ResponseEntity<IncomeSummaryResponse> getSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "MONTH") String period,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().year}") int year,
            @RequestParam(required = false) Integer month) {
        
        if (month == null) {
            month = LocalDate.now().getMonthValue();
        }
        
        return ResponseEntity.ok(analyticsService.getSummary(
                userDetails.getUserId(), period, year, month));
    }

    @GetMapping("/trends")
    public ResponseEntity<IncomeTrendsResponse> getTrends(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "12") int months) {
        
        return ResponseEntity.ok(analyticsService.getTrends(
                userDetails.getUserId(), months));
    }

    @GetMapping("/forecast")
    public ResponseEntity<IncomeForecastResponse> getForecast(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "6") int months) {
        
        return ResponseEntity.ok(analyticsService.getForecast(
                userDetails.getUserId(), months));
    }

    @GetMapping("/comparison")
    public ResponseEntity<IncomeComparisonResponse> getComparison(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().year}") int currentYear,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().year - 1}") int previousYear) {
        
        return ResponseEntity.ok(analyticsService.compareYears(
                userDetails.getUserId(), currentYear, previousYear));
    }

    @GetMapping("/by-category")
    public ResponseEntity<IncomeByCategoryResponse> getByCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "MONTH") String period,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().year}") int year,
            @RequestParam(required = false) Integer month) {
        
        if (month == null) {
            month = LocalDate.now().getMonthValue();
        }
        
        return ResponseEntity.ok(analyticsService.getByCategory(
                userDetails.getUserId(), period, year, month));
    }
}
