package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Budget;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @EntityGraph(attributePaths = {"category"})
    List<Budget> findByUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = {"category"})
    Optional<Budget> findByIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = {"category"})
    List<Budget> findByUserIdOrPartnerIdOrderByCreatedAtDesc(Long userId, Long partnerId);

    @EntityGraph(attributePaths = {"category"})
    Optional<Budget> findByIdAndUserIdOrIdAndPartnerId(Long id1, Long userId, Long id2, Long partnerId);

    @Query("""
        SELECT COUNT(b) > 0 FROM Budget b
        WHERE b.userId = :userId
          AND b.id != :excludeId
          AND b.startsAt <= :endsAt
          AND b.endsAt >= :startsAt
          AND b.scopeType = :scopeType
          AND (
            (:scopeType = 'category' AND b.category.id = :categoryId)
            OR (:scopeType = 'group' AND b.group.id = :groupId)
            OR (:scopeType = 'region' AND LOWER(b.region) = LOWER(:region))
            OR (:scopeType = 'city' AND LOWER(b.city) = LOWER(:city))
            OR (:scopeType = 'country' AND LOWER(b.country) = LOWER(:country))
          )
          AND (b.locationType = :locationType OR (b.locationType IS NULL AND :locationType IS NULL))
          AND (LOWER(b.locationValue) = :locationValue OR (b.locationValue IS NULL AND :locationValue IS NULL))
        """)
    boolean existsOverlappingBudget(
            Long userId,
            Long excludeId,
            LocalDate startsAt,
            LocalDate endsAt,
            String scopeType,
            Long categoryId,
            Long groupId,
            String region,
            String city,
            String country,
            String locationType,
            String locationValue
    );
}
