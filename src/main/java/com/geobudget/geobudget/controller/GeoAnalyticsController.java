package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.analytics.GeoCityAnalyticsDetailResponse;
import com.geobudget.geobudget.dto.analytics.GeoCityAnalyticsResponse;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.GeoAnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analytics/geo")
@PreAuthorize("isAuthenticated()")
public class GeoAnalyticsController {
    private final GeoAnalyticsService geoAnalyticsService;

    public GeoAnalyticsController(GeoAnalyticsService geoAnalyticsService) {
        this.geoAnalyticsService = geoAnalyticsService;
    }

    @GetMapping("/cities")
    public ResponseEntity<List<GeoCityAnalyticsResponse>> getCities(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(geoAnalyticsService.getCities(userDetails.getUserId(), type, from, to));
    }

    @GetMapping("/city/{key}")
    public ResponseEntity<GeoCityAnalyticsDetailResponse> getCityDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String key,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(geoAnalyticsService.getCityDetail(userDetails.getUserId(), key, type, from, to));
    }
}
