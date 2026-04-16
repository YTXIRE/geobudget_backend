package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.tag.*;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagDto>> getTags(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tagService.getTagsByUser(userDetails.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTag(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tagService.getTagById(id, userDetails.getUserId()));
    }

    @PostMapping
    public ResponseEntity<TagDto> createTag(
            @RequestBody CreateTagRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tagService.createTag(request, userDetails.getUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateTag(
            @PathVariable Long id,
            @RequestBody UpdateTagRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tagService.updateTag(id, request, userDetails.getUserId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        tagService.deleteTag(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transaction/{transactionId}")
    public ResponseEntity<Void> addTagsToTransaction(
            @PathVariable Long transactionId,
            @RequestBody List<Long> tagIds,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        tagService.addTagsToTransaction(transactionId, tagIds, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<TagDto>> getTagsByTransaction(
            @PathVariable Long transactionId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tagService.getTagsByTransaction(transactionId, userDetails.getUserId()));
    }

    @GetMapping("/stats/summary")
    public ResponseEntity<TagStatsResponse> getTagStats(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(tagService.getTagStats(userDetails.getUserId(), from, to, limit));
    }
}
