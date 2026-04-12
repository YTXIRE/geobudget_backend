package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.GroupDto;
import com.geobudget.geobudget.security.CustomUserDetails;
import com.geobudget.geobudget.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/groups")
    public ResponseEntity<List<GroupDto>> getGroups(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(groupService.getGroupsForUser(userDetails.getUserId()));
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupDto> getGroup(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(groupService.getGroupById(id, userDetails.getUserId()));
    }

    @PostMapping("/groups")
    public ResponseEntity<GroupDto> createGroup(@Valid @RequestBody GroupDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(dto, userDetails.getUserId()));
    }

    @PutMapping("/groups/{id}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable Long id, @Valid @RequestBody GroupDto dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(groupService.updateGroup(id, dto, userDetails.getUserId()));
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        groupService.deleteGroup(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
