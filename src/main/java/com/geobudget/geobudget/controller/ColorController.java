package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.ColorGroupDto;
import com.geobudget.geobudget.service.ColorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/colors")
@Tag(name = "Colors", description = "Работа с цветами")
public class ColorController {
    private final ColorService colorService;

    @GetMapping
    public ResponseEntity<List<ColorGroupDto>> getAllColors() {
        return ResponseEntity.ok(colorService.getAllColors());
    }
}
