package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Budget;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
