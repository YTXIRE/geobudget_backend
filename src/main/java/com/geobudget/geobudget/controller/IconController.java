package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.IconGroupDto;
import com.geobudget.geobudget.service.IconService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/icons")
@Tag(name = "Icons", description = "Работа с иконками")
public class IconController {
    private final IconService iconService;

    @GetMapping
    public ResponseEntity<List<IconGroupDto>> getAllIcons() {
        return ResponseEntity.ok(iconService.getAllIcons());
    }
}
