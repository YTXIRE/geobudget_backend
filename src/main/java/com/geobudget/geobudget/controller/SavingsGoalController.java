package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.savings.*;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.SavingsGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/savings-goals")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    @GetMapping
    public ResponseEntity<GoalsListResponse> getGoals(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(savingsGoalService.getGoalsByUser(userDetails.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> getGoal(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(savingsGoalService.getGoalById(id, userDetails.getUserId()));
    }

    @PostMapping
    public ResponseEntity<SavingsGoalResponse> createGoal(
            @Valid @RequestBody CreateGoalRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savingsGoalService.createGoal(userDetails.getUserId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingsGoalResponse> updateGoal(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGoalRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(savingsGoalService.updateGoal(id, userDetails.getUserId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        savingsGoalService.deleteGoal(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/contribute")
    public ResponseEntity<GoalContributionResponse> contribute(
            @PathVariable Long id,
            @Valid @RequestBody ContributeRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savingsGoalService.contribute(id, userDetails.getUserId(), request));
    }

    @PatchMapping("/{id}/pause")
    public ResponseEntity<SavingsGoalResponse> pauseGoal(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(savingsGoalService.pauseGoal(id, userDetails.getUserId()));
    }

    @PatchMapping("/{id}/resume")
    public ResponseEntity<SavingsGoalResponse> resumeGoal(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(savingsGoalService.resumeGoal(id, userDetails.getUserId()));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<SavingsGoalResponse> completeGoal(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(savingsGoalService.completeGoal(id, userDetails.getUserId()));
    }

    @GetMapping("/{id}/contributions")
    public ResponseEntity<List<GoalContributionResponse>> getContributions(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(savingsGoalService.getContributions(id, userDetails.getUserId()));
    }

    @GetMapping("/{id}/milestones")
    public ResponseEntity<List<GoalMilestoneResponse>> getMilestones(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(savingsGoalService.getMilestones(id, userDetails.getUserId()));
    }

    @PostMapping("/{id}/milestones")
    public ResponseEntity<GoalMilestoneResponse> addMilestone(
            @PathVariable Long id,
            @Valid @RequestBody CreateMilestoneRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savingsGoalService.addMilestone(id, userDetails.getUserId(), request));
    }

    @PatchMapping("/{goalId}/milestones/{milestoneId}/complete")
    public ResponseEntity<GoalMilestoneResponse> completeMilestone(
            @PathVariable Long goalId,
            @PathVariable Long milestoneId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(savingsGoalService.completeMilestone(milestoneId, goalId, userDetails.getUserId()));
    }

    @DeleteMapping("/{goalId}/milestones/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(
            @PathVariable Long goalId,
            @PathVariable Long milestoneId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        savingsGoalService.deleteMilestone(milestoneId, goalId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
