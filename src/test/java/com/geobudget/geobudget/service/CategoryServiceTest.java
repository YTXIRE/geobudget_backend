package com.geobudget.geobudget.service;

import com.geobudget.geobudget.config.CategoryProperties;
import com.geobudget.geobudget.dto.CategoryDto;
import com.geobudget.geobudget.dto.companyInfo.CompanyInfo;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.ColorRepository;
import com.geobudget.geobudget.repository.GroupRepository;
import com.geobudget.geobudget.repository.IconRepository;
import com.geobudget.geobudget.repository.OkvedRepository;
import com.geobudget.geobudget.repository.ReceiptRepository;
import com.geobudget.geobudget.validator.InnValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    private DadataService dadataService;
    private OkvedRepository okvedRepository;
    private CategoryRepository categoryRepository;
    private CategoryProperties categoryProperties;
    private InnValidator innValidator;
    private IconRepository iconRepository;
    private ColorRepository colorRepository;
    private GroupRepository groupRepository;
    private ReceiptRepository receiptRepository;
    private CategoryService service;

    @BeforeEach
    void setUp() throws Exception {
        dadataService = Mockito.mock(DadataService.class);
        okvedRepository = Mockito.mock(OkvedRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryProperties = new CategoryProperties();
        innValidator = new InnValidator();
        iconRepository = Mockito.mock(IconRepository.class);
        colorRepository = Mockito.mock(ColorRepository.class);
        groupRepository = Mockito.mock(GroupRepository.class);
        receiptRepository = Mockito.mock(ReceiptRepository.class);
        service = new CategoryService(
                dadataService,
                okvedRepository,
                categoryRepository,
                categoryProperties,
                innValidator,
                iconRepository,
                colorRepository,
                groupRepository,
                receiptRepository
        );
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
        when(categoryRepository.findById(categoryProperties.getFallbackId().longValue())).thenReturn(Optional.of(fallback));

        CategoryDto dto = service.getCategory("1234567890");
        assertEquals(fallback.getId(), dto.getId());
        assertEquals(fallback.getName(), dto.getName());
    }
}

