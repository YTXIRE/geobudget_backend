package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.transaction.TransactionCreateRequest;
import com.geobudget.geobudget.dto.transaction.TransactionIncomeCreateRequest;
import com.geobudget.geobudget.dto.transaction.TransactionResponse;
import com.geobudget.geobudget.dto.transaction.TransactionStatsResponse;
import com.geobudget.geobudget.dto.transaction.TransactionSummaryResponse;
import com.geobudget.geobudget.dto.transaction.TransactionUpdateRequest;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest httpRequest,
            @Valid @RequestBody TransactionCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.create(userDetails.getUserId(), request, httpRequest));
    }

    @PostMapping("/income")
    public ResponseEntity<TransactionResponse> createIncome(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest httpRequest,
            @Valid @RequestBody TransactionIncomeCreateRequest request
    ) {
        TransactionCreateRequest incomeRequest = TransactionCreateRequest.builder()
                .type("income")
                .amount(request.getAmount())
                .categoryId(request.getCategoryId())
                .description(request.getDescription())
                .occurredAt(request.getOccurredAt())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .city(request.getCity())
                .country(request.getCountry())
                .region(request.getRegion())
                .placeId(request.getPlaceId())
                .locationSource(request.getLocationSource())
                .originalAmount(request.getOriginalAmount())
                .originalCurrency(request.getOriginalCurrency())
                .rateToBase(request.getRateToBase())
                .baseAmount(request.getBaseAmount())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.create(userDetails.getUserId(), incomeRequest, httpRequest));
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAll(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) List<Long> tagIds,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false, defaultValue = "false") boolean includePartners,
            @PageableDefault(size = 20, sort = "occurredAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(transactionService.getAll(
                userDetails.getUserId(), type, from, to, categoryId, tagIds, city, country, partnerId, includePartners, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(transactionService.getById(userDetails.getUserId(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody TransactionUpdateRequest request
    ) {
        return ResponseEntity.ok(transactionService.update(userDetails.getUserId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id
    ) {
        transactionService.softDelete(userDetails.getUserId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryResponse> getSummary(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(transactionService.getSummary(userDetails.getUserId(), from, to));
    }

    @GetMapping("/stats/overview")
    public ResponseEntity<TransactionStatsResponse> getOverviewStats(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(transactionService.getOverviewStats(userDetails.getUserId()));
    }

    @GetMapping("/stats/income")
    public ResponseEntity<TransactionStatsResponse> getIncomeStats(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(transactionService.getIncomeStats(userDetails.getUserId()));
    }

    @GetMapping("/stats/expense")
    public ResponseEntity<TransactionStatsResponse> getExpenseStats(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(transactionService.getExpenseStats(userDetails.getUserId()));
    }

    @GetMapping("/partner/{partnerId}")
    public ResponseEntity<Page<TransactionResponse>> getPartnerTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long partnerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 20, sort = "occurredAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(transactionService.getPartnerTransactions(
                userDetails.getUserId(), partnerId, from, to, pageable));
    }
}
