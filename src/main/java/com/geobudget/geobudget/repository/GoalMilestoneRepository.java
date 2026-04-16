package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.GoalMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoalMilestoneRepository extends JpaRepository<GoalMilestone, Long> {

    List<GoalMilestone> findByGoalIdOrderByOrderIndexAsc(Long goalId);

    Optional<GoalMilestone> findByIdAndGoalId(Long id, Long goalId);

    @Query("SELECT m FROM GoalMilestone m WHERE m.goal.id = :goalId AND m.isCompleted = false ORDER BY m.orderIndex ASC")
    List<GoalMilestone> findIncompleteByGoalId(@Param("goalId") Long goalId);

    @Query("SELECT m FROM GoalMilestone m WHERE m.goal.id = :goalId AND m.isCompleted = false ORDER BY m.orderIndex ASC LIMIT 1")
    Optional<GoalMilestone> findNextMilestoneByGoalId(@Param("goalId") Long goalId);

    @Modifying
    @Query("UPDATE GoalMilestone m SET m.isCompleted = true, m.completedAt = CURRENT_TIMESTAMP WHERE m.id = :id")
    void markAsCompleted(@Param("id") Long id);

    Long countByGoalId(Long goalId);
}
