package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.docs.receipt.CheckDoc;
import com.geobudget.geobudget.dto.checkReceipt.CheckReceipt;
import com.geobudget.geobudget.dto.receipt.ReceiptCategoryPreferenceRequest;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.CheckReceiptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/receipt")
@Tag(name = "Receipt", description = "Работа с чеками")
@Validated
public class ReceiptController {
    private final CheckReceiptService checkReceiptService;

    @CheckDoc
    @PostMapping("/check")
    public ResponseEntity<CheckReceipt> checkReceipt(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Map<String, String> request
    ) throws Exception {
        String qr = request.get("qr");
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        CheckReceipt result = checkReceiptService.checkReceipt(qr, userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/category-preference")
    public ResponseEntity<Void> saveCategoryPreference(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReceiptCategoryPreferenceRequest request
    ) {
        checkReceiptService.saveCategoryPreference(userDetails.getUserId(), request.inn(), request.categoryId());
        return ResponseEntity.ok().build();
    }
}
