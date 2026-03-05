package com.geobudget.geobudget.service;

import com.geobudget.geobudget.config.CategoryProperties;
import com.geobudget.geobudget.dto.CategoryDto;
import com.geobudget.geobudget.dto.ColorDto;
import com.geobudget.geobudget.dto.IconDto;
import com.geobudget.geobudget.dto.companyInfo.Suggestion;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Color;
import com.geobudget.geobudget.entity.Icon;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.ColorRepository;
import com.geobudget.geobudget.repository.IconRepository;
import com.geobudget.geobudget.repository.OkvedRepository;
import com.geobudget.geobudget.repository.ReceiptRepository;
import com.geobudget.geobudget.validator.InnValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final IconRepository iconRepository;
    private final ColorRepository colorRepository;
    private final ReceiptRepository receiptRepository;

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
            Category categoryEntity = categoryRepository.findById(categoryProperties.getFallbackId().longValue())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            categoryDto = CategoryDto.builder().id(categoryEntity.getId()).name(categoryEntity.getName()).description(categoryEntity.getDescription()).build();
        }

        return categoryDto;
    }

    public List<CategoryDto> getCategoriesForUser(Long userId) {
        List<Category> systemCategories = categoryRepository.findAllSystemCategories();
        List<Category> userCategories = categoryRepository.findUserCategoriesByUserId(userId);
        
        List<Category> allCategories = new java.util.ArrayList<>();
        allCategories.addAll(systemCategories);
        allCategories.addAll(userCategories);
        
        return allCategories.stream()
                .map(category -> {
                    Integer transactionCount = receiptRepository.countByCategoryId(category.getId());
                    Double totalSum = receiptRepository.sumTotalSumByCategoryId(category.getId());
                    return CategoryDto.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .description(category.getDescription())
                            .icon(category.getIcon() != null ? mapToIconDto(category.getIcon()) : null)
                            .color(category.getColor() != null ? mapToColorDto(category.getColor()) : null)
                            .isFavorite(category.getIsFavorite())
                            .group(category.getGroupName())
                            .type(category.getType())
                            .userId(category.getUserId())
                            .parentId(category.getParentId())
                            .transactionCount(transactionCount)
                            .totalSum(BigDecimal.valueOf(totalSum != null ? totalSum : 0.0))
                            .createdAt(category.getCreatedAt())
                            .updatedAt(category.getUpdatedAt())
                            .build();
                })
                .toList();
    }

    public CategoryDto createCategory(CategoryDto dto, Long userId) {
        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            if (parent.getParentId() != null) {
                throw new RuntimeException("Cannot create subcategory: maximum nesting depth is 2 levels");
            }
        }
        
        Icon icon = dto.getIcon() != null && dto.getIcon().getId() != null
                ? iconRepository.findById(dto.getIcon().getId()).orElse(null) 
                : iconRepository.findById(1L).orElse(null);
        Color color = dto.getColor() != null && dto.getColor().getId() != null
                ? colorRepository.findById(dto.getColor().getId()).orElse(null) 
                : colorRepository.findById(1L).orElse(null);
        
        Category parentCategory = dto.getParentId() != null 
                ? categoryRepository.findById(dto.getParentId()).orElse(null) 
                : null;
        
        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .icon(icon)
                .color(color)
                .groupName(dto.getGroup() != null ? dto.getGroup() : (parentCategory != null ? parentCategory.getGroupName() : null))
                .type("user")
                .userId(userId)
                .parentId(dto.getParentId())
                .build();
        return getCategoryDto(category);
    }

    public CategoryDto updateCategory(Long id, CategoryDto dto, Long userId) {
        Category category = categoryRepository.findByIdWithIconAndColor(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        if (!"system".equals(category.getType()) && !userId.equals(category.getUserId())) {
            throw new RuntimeException("You can only edit your own categories");
        }
        
        if (dto.getParentId() != null && !dto.getParentId().equals(category.getId())) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            if (parent.getParentId() != null) {
                throw new RuntimeException("Maximum nesting depth is 2 levels");
            }
            category.setGroupName(dto.getGroup() != null ? dto.getGroup() : parent.getGroupName());
        } else if (dto.getParentId() == null) {
            category.setGroupName(dto.getGroup() != null ? dto.getGroup() : category.getGroupName());
        }
        
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        if (dto.getIcon() != null && dto.getIcon().getId() != null) {
            category.setIcon(iconRepository.findById(dto.getIcon().getId()).orElse(null));
        } else {
            category.setIcon(iconRepository.findById(1L).orElse(null));
        }
        if (dto.getColor() != null && dto.getColor().getId() != null) {
            category.setColor(colorRepository.findById(dto.getColor().getId()).orElse(null));
        } else {
            category.setColor(colorRepository.findById(1L).orElse(null));
        }
        return getCategoryDto(category);
    }

    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if ("system".equals(category.getType())) {
            throw new RuntimeException("Cannot delete system category");
        }
        if (!userId.equals(category.getUserId())) {
            throw new RuntimeException("You can only delete your own categories");
        }
        
        List<Category> subcategories = categoryRepository.findByParentId(id);
        for (Category subcategory : subcategories) {
            subcategory.setParentId(null);
            categoryRepository.save(subcategory);
        }
        
        categoryRepository.deleteById(id);
    }

    public CategoryDto toggleFavorite(Long id, Long userId) {
        Category category = categoryRepository.findByIdWithIconAndColor(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        if (!"system".equals(category.getType()) && !userId.equals(category.getUserId())) {
            throw new RuntimeException("You can only toggle favorite on your own categories");
        }
        
        boolean currentValue = category.getIsFavorite() != null && category.getIsFavorite();
        category.setIsFavorite(!currentValue);
        return getCategoryDto(category);
    }

    private CategoryDto getCategoryDto(Category category) {
        category = categoryRepository.save(category);
        Integer transactionCount = receiptRepository.countByCategoryId(category.getId());
        Double totalSum = receiptRepository.sumTotalSumByCategoryId(category.getId());
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .icon(category.getIcon() != null ? mapToIconDto(category.getIcon()) : null)
                .color(category.getColor() != null ? mapToColorDto(category.getColor()) : null)
                .isFavorite(category.getIsFavorite())
                .group(category.getGroupName())
                .type(category.getType())
                .userId(category.getUserId())
                .parentId(category.getParentId())
                .transactionCount(transactionCount)
                .totalSum(BigDecimal.valueOf(totalSum != null ? totalSum : 0.0))
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    private IconDto mapToIconDto(Icon icon) {
        return IconDto.builder()
                .id(icon.getId())
                .name(icon.getName())
                .codePoint(icon.getCodePoint())
                .build();
    }

    private ColorDto mapToColorDto(Color color) {
        return ColorDto.builder()
                .id(color.getId())
                .name(color.getName())
                .hex(color.getHex())
                .argb(color.getArgb())
                .build();
    }
}
