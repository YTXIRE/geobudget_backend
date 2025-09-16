package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.docs.companyInfo.GetCompanyByInnDoc;
import com.geobudget.geobudget.dto.CategoryDto;
import com.geobudget.geobudget.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
@Tag(name = "Category", description = "Работа с категориями")
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetCompanyByInnDoc
    @GetMapping("/get-category-by-inn")
    public ResponseEntity<CategoryDto> getCategoryByInn(
            @RequestParam
            @NotBlank(message = "ИНН обязателен")
            @Pattern(regexp = "^\\d{10}|\\d{12}$", message = "ИНН должен состоять из 10 или 12 цифр") String inn) throws Exception {
        return ResponseEntity.ok(categoryService.getCategory(inn));
    }
}
