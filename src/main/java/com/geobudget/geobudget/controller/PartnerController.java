package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.PartnerDto;
import com.geobudget.geobudget.dto.PartnerInviteRequest;
import com.geobudget.geobudget.dto.PartnerStatsResponse;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.PartnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/partners")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class PartnerController {
    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPartners(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<PartnerDto> accepted = partnerService.getAcceptedPartners(userDetails.getUserId());
        List<PartnerDto> pendingSent = partnerService.getPendingInvitations(userDetails.getUserId());
        List<PartnerDto> pendingReceived = partnerService.getPendingIncoming(userDetails.getUserId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("accepted", accepted);
        response.put("pendingSent", pendingSent);
        response.put("pendingReceived", pendingReceived);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accepted")
    public ResponseEntity<List<PartnerDto>> getAcceptedPartners(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(partnerService.getAcceptedPartners(userDetails.getUserId()));
    }

    @GetMapping("/{partnerId}/stats")
    public ResponseEntity<PartnerStatsResponse> getPartnerStats(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long partnerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(partnerService.getPartnerStats(
                userDetails.getUserId(), partnerId, from, to));
    }

    @GetMapping("/budgets/shared")
    public ResponseEntity<Map<String, Object>> getSharedBudgets(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(partnerService.getSharedBudgets(userDetails.getUserId()));
    }

    @PostMapping("/invite")
    public ResponseEntity<PartnerDto> invitePartner(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PartnerInviteRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(partnerService.invitePartner(userDetails.getUserId(), request));
    }

    @PostMapping("/{partnerId}/accept")
    public ResponseEntity<PartnerDto> acceptInvitation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long partnerId
    ) {
        return ResponseEntity.ok(partnerService.acceptInvitation(userDetails.getUserId(), partnerId));
    }

    @PostMapping("/{partnerId}/reject")
    public ResponseEntity<PartnerDto> rejectInvitation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long partnerId
    ) {
        return ResponseEntity.ok(partnerService.rejectInvitation(userDetails.getUserId(), partnerId));
    }

    @DeleteMapping("/{partnerId}")
    public ResponseEntity<Void> removePartner(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long partnerId
    ) {
        partnerService.removePartner(userDetails.getUserId(), partnerId);
        return ResponseEntity.noContent().build();
    }
}
