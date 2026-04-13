package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.TransactionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionTemplateRepository extends JpaRepository<TransactionTemplate, Long> {
    List<TransactionTemplate> findByUserIdAndIsActiveTrueOrderByNameAsc(Long userId);
    List<TransactionTemplate> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<TransactionTemplate> findByUserIdAndIsActiveTrueAndRecurrenceEnabledTrue(Long userId);
    List<TransactionTemplate> findByIsActiveTrueAndRecurrenceEnabledTrue();
}
