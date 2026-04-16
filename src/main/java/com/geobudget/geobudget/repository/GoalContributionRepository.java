package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.GoalContribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GoalContributionRepository extends JpaRepository<GoalContribution, Long> {

    List<GoalContribution> findByGoalIdOrderByContributionDateDesc(Long goalId);

    @Query("SELECT c FROM GoalContribution c WHERE c.goal.userId = :userId ORDER BY c.contributionDate DESC")
    List<GoalContribution> findAllByUserIdOrderByDateDesc(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM GoalContribution c WHERE c.goal.id = :goalId")
    BigDecimal sumAmountByGoalId(@Param("goalId") Long goalId);

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM GoalContribution c " +
           "WHERE c.goal.id = :goalId AND c.contributionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByGoalIdAndDateRange(
            @Param("goalId") Long goalId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM GoalContribution c " +
           "WHERE c.goal.userId = :userId AND c.contributionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(c) FROM GoalContribution c WHERE c.goal.id = :goalId")
    Long countByGoalId(@Param("goalId") Long goalId);
}
