package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    List<SavingsGoal> findByUserIdOrderByPriorityAscCreatedAtDesc(Long userId);

    List<SavingsGoal> findByUserIdAndStatus(Long userId, SavingsGoal.GoalStatus status);

    Optional<SavingsGoal> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT g FROM SavingsGoal g WHERE g.userId = :userId AND g.status = 'ACTIVE' ORDER BY g.priority ASC")
    List<SavingsGoal> findActiveGoalsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(g) FROM SavingsGoal g WHERE g.userId = :userId AND g.status = 'COMPLETED'")
    Long countCompletedByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(g) FROM SavingsGoal g WHERE g.userId = :userId AND g.status = 'ACTIVE'")
    Long countActiveByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(g.currentAmount) FROM SavingsGoal g WHERE g.userId = :userId")
    java.math.BigDecimal sumCurrentAmountByUserId(@Param("userId") Long userId);

    @Query("SELECT SUM(g.targetAmount) FROM SavingsGoal g WHERE g.userId = :userId AND g.status != 'CANCELLED'")
    java.math.BigDecimal sumTargetAmountByUserId(@Param("userId") Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
}
