package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.TransactionTemplateDto;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.TransactionTemplate;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.TransactionTemplateRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionTemplateService {
    
    private final TransactionTemplateRepository templateRepository;
    private final CategoryRepository categoryRepository;

    public TransactionTemplateService(
            TransactionTemplateRepository templateRepository,
            CategoryRepository categoryRepository) {
        this.templateRepository = templateRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionTemplateDto> getTemplates(Long userId) {
        return templateRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionTemplate> getRecurringTemplatesForDay(Long userId, int dayOfWeek) {
        return templateRepository.findByUserIdAndIsActiveTrueAndRecurrenceEnabledTrue(userId).stream()
                .filter(t -> isDayMatch(t.getRecurrenceDays(), dayOfWeek))
                .toList();
    }

    private boolean isDayMatch(String recurrenceDays, int dayOfWeek) {
        if (recurrenceDays == null || recurrenceDays.isEmpty()) {
            return false;
        }
        String[] days = recurrenceDays.split(",");
        for (String day : days) {
            try {
                if (Integer.parseInt(day.trim()) == dayOfWeek) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    @Transactional
    public TransactionTemplateDto create(Long userId, String name, String type, 
            Long categoryId, BigDecimal amount, String currency, String description,
            Boolean recurrenceEnabled, String recurrenceType, String recurrenceDays, Integer dayOfMonth,
            String city, String country, String region,
            Double latitude, Double longitude, String placeId, String locationSource) {
        
        Category category = categoryId != null 
                ? categoryRepository.findById(categoryId).orElse(null)
                : null;

        TransactionTemplate template = new TransactionTemplate();
        template.setUserId(userId);
        template.setName(name);
        template.setType(type);
        template.setCategory(category);
        template.setAmount(amount);
        template.setCurrency(currency);
        template.setDescription(description);
        template.setIsActive(true);
        template.setRecurrenceEnabled(recurrenceEnabled != null && recurrenceEnabled);
        template.setRecurrenceType(recurrenceType);
        template.setRecurrenceDays(recurrenceDays);
        template.setDayOfMonth(dayOfMonth);
        template.setCity(city);
        template.setCountry(country);
        template.setRegion(region);
        template.setLatitude(latitude);
        template.setLongitude(longitude);
        template.setPlaceId(placeId);
        template.setLocationSource(locationSource);

        return toDto(templateRepository.save(template));
    }

    @Transactional
    public TransactionTemplateDto update(Long userId, Long id, String name, String type,
            Long categoryId, BigDecimal amount, String currency, String description,
            Boolean recurrenceEnabled, String recurrenceType, String recurrenceDays, Integer dayOfMonth,
            String city, String country, String region,
            Double latitude, Double longitude, String placeId, String locationSource) {
        
        TransactionTemplate template = templateRepository.findById(id)
                .filter(t -> t.getUserId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("Template not found"));

        Category category = categoryId != null
                ? categoryRepository.findById(categoryId).orElse(null)
                : null;

        template.setName(name);
        template.setType(type);
        template.setCategory(category);
        template.setAmount(amount);
        template.setCurrency(currency);
        template.setDescription(description);
        template.setRecurrenceEnabled(recurrenceEnabled != null && recurrenceEnabled);
        template.setRecurrenceType(recurrenceType);
        template.setRecurrenceDays(recurrenceDays);
        template.setDayOfMonth(dayOfMonth);
        template.setCity(city);
        template.setCountry(country);
        template.setRegion(region);
        template.setLatitude(latitude);
        template.setLongitude(longitude);
        template.setPlaceId(placeId);
        template.setLocationSource(locationSource);

        return toDto(templateRepository.save(template));
    }

    @Transactional
    public void delete(Long userId, Long id) {
        TransactionTemplate template = templateRepository.findById(id)
                .filter(t -> t.getUserId().equals(userId))
                .orElseThrow(() -> new EntityNotFoundException("Template not found"));
        template.setIsActive(false);
        templateRepository.save(template);
    }

    private TransactionTemplateDto toDto(TransactionTemplate template) {
        Category category = template.getCategory();
        return new TransactionTemplateDto(
                template.getId(),
                template.getUserId(),
                template.getName(),
                template.getType(),
                category != null ? category.getId() : null,
                category != null ? category.getName() : null,
                category != null && category.getIcon() != null ? category.getIcon().toString() : null,
                template.getAmount(),
                template.getCurrency(),
                template.getDescription(),
                template.getIsActive(),
                template.getRecurrenceEnabled(),
                template.getRecurrenceType(),
                template.getRecurrenceDays(),
                template.getDayOfMonth(),
                template.getCity(),
                template.getCountry(),
                template.getRegion(),
                template.getLatitude(),
                template.getLongitude(),
                template.getPlaceId(),
                template.getLocationSource()
        );
    }
}
