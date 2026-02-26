package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.docs.companyInfo.GetCompanyByInnDoc;
import com.geobudget.geobudget.dto.CategoryDto;
import com.geobudget.geobudget.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Category", description = "Работа с категориями")
@Validated
@PreAuthorize("hasRole('USER')")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(dto));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto dto) {
        log.info("DTO: {}", dto);
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetCompanyByInnDoc
    @GetMapping("/category/get-category-by-inn")
    public ResponseEntity<CategoryDto> getCategoryByInn(
            @RequestParam
            @NotBlank(message = "ИНН обязателен")
            @Pattern(regexp = "^\\d{10}|\\d{12}$", message = "ИНН должен состоять из 10 или 12 цифр") String inn) throws Exception {
        return ResponseEntity.ok(categoryService.getCategory(inn));
    }

    @PostMapping("/categories/{id}/favorite")
    public ResponseEntity<CategoryDto> toggleFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.toggleFavorite(id));
    }
}
