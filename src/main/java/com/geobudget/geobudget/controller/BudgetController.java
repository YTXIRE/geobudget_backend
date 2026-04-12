package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.budget.BudgetProgressResponse;
import com.geobudget.geobudget.dto.budget.BudgetRequest;
import com.geobudget.geobudget.dto.budget.BudgetResponse;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(budgetService.getBudgets(userDetails.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudget(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getBudget(userDetails.getUserId(), id));
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody BudgetRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(userDetails.getUserId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request
    ) {
        return ResponseEntity.ok(budgetService.updateBudget(userDetails.getUserId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        budgetService.deleteBudget(userDetails.getUserId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/progress")
    public ResponseEntity<List<BudgetProgressResponse>> getProgress(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(budgetService.getBudgetProgress(userDetails.getUserId()));
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<BudgetProgressResponse> getProgressById(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getBudgetProgress(userDetails.getUserId(), id));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<BudgetResponse> archiveBudget(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        return ResponseEntity.ok(budgetService.toggleArchive(userDetails.getUserId(), id));
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<BudgetResponse> duplicateBudget(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.duplicateBudget(userDetails.getUserId(), id));
    }
}
