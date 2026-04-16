package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.subscription.*;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<SubscriptionsListResponse> getSubscriptions(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.getSubscriptions(userDetails.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getSubscription(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.getSubscription(id, userDetails.getUserId()));
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subscriptionService.createSubscription(userDetails.getUserId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSubscriptionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(id, userDetails.getUserId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        subscriptionService.deleteSubscription(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pause")
    public ResponseEntity<SubscriptionResponse> pauseSubscription(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.pauseSubscription(id, userDetails.getUserId()));
    }

    @PatchMapping("/{id}/resume")
    public ResponseEntity<SubscriptionResponse> resumeSubscription(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.resumeSubscription(id, userDetails.getUserId()));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(id, userDetails.getUserId()));
    }

    @PatchMapping("/{id}/mark-paid")
    public ResponseEntity<SubscriptionResponse> markPaid(
            @PathVariable Long id,
            HttpServletRequest httpRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(subscriptionService.markPaid(id, userDetails.getUserId(), httpRequest));
    }
}
