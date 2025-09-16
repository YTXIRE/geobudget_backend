package com.geobudget.geobudget.service;

import com.geobudget.geobudget.config.CategoryProperties;
import com.geobudget.geobudget.dto.CategoryDto;
import com.geobudget.geobudget.dto.companyInfo.Suggestion;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.OkvedRepository;
import com.geobudget.geobudget.validator.InnValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final DadataService dadataService;
    private final OkvedRepository okvedRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryProperties categoryProperties;
    private final InnValidator innValidator;

    public CategoryDto getCategory(String inn) throws Exception {
        innValidator.validateInn(inn);
        CategoryDto categoryDto = null;

        List<Suggestion> foundedOkvedSuggestions = dadataService.getCompanyByInn(inn).getSuggestions();

        if (!foundedOkvedSuggestions.isEmpty()) {
            String foundedOkved = foundedOkvedSuggestions.getFirst().getData().getOkved();

            categoryDto = okvedRepository.findByCode(foundedOkved)
                    .map(okved -> CategoryDto.builder().id(okved.getCategory().getId()).name(okved.getCategory().getName()).description(okved.getCategory().getDescription()).build())
                    .orElse(null);
        }

        if (categoryDto == null) {
            Category categoryEntity = categoryRepository.findById(categoryProperties.getFallbackId());
            categoryDto = CategoryDto.builder().id(categoryEntity.getId()).name(categoryEntity.getName()).description(categoryEntity.getDescription()).build();
        }

        return categoryDto;
    }
}
