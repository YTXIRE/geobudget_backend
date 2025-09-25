package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.docs.receipt.CheckDoc;
import com.geobudget.geobudget.dto.checkReceipt.CheckReceipt;
import com.geobudget.geobudget.service.CheckReceiptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<CheckReceipt> checkReceipt(@RequestBody Map<String, String> request) throws Exception {
        String qr = request.get("qr");
        CheckReceipt result = checkReceiptService.checkReceipt(qr);
        return ResponseEntity.ok(result);
    }
}
