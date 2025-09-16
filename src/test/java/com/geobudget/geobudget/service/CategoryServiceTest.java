package com.geobudget.geobudget.service;

import com.geobudget.geobudget.config.CategoryProperties;
import com.geobudget.geobudget.dto.CategoryDto;
import com.geobudget.geobudget.dto.companyInfo.CompanyInfo;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.OkvedRepository;
import com.geobudget.geobudget.validator.InnValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    private DadataService dadataService;
    private OkvedRepository okvedRepository;
    private CategoryRepository categoryRepository;
    private CategoryProperties categoryProperties;
    private InnValidator innValidator;
    private CategoryService service;

    @BeforeEach
    void setUp() throws Exception {
        dadataService = Mockito.mock(DadataService.class);
        okvedRepository = Mockito.mock(OkvedRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryProperties = new CategoryProperties();
        innValidator = new InnValidator();
        service = new CategoryService(dadataService, okvedRepository, categoryRepository, categoryProperties, innValidator);
    }

    @Test
    void getCategory_returnsFallbackWhenNotFound() throws Exception {
        CompanyInfo info = new CompanyInfo();
        info.setSuggestions(List.of());
        when(dadataService.getCompanyByInn("1234567890")).thenReturn(info);
        Category fallback = new Category();
        fallback.setId(Long.valueOf(categoryProperties.getFallbackId()));
        fallback.setName("Другое");
        fallback.setDescription("Категория по умолчанию");
        when(categoryRepository.findById(categoryProperties.getFallbackId())).thenReturn(fallback);

        CategoryDto dto = service.getCategory("1234567890");
        assertEquals(fallback.getId(), dto.getId());
        assertEquals(fallback.getName(), dto.getName());
    }
}


