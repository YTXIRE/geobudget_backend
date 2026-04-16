package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.savings.*;
import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.GoalContribution;
import com.geobudget.geobudget.entity.GoalMilestone;
import com.geobudget.geobudget.entity.SavingsGoal;
import com.geobudget.geobudget.repository.CategoryRepository;
import com.geobudget.geobudget.repository.GoalContributionRepository;
import com.geobudget.geobudget.repository.GoalMilestoneRepository;
import com.geobudget.geobudget.repository.SavingsGoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavingsGoalService {

    private final SavingsGoalRepository goalRepository;
    private final GoalContributionRepository contributionRepository;
    private final GoalMilestoneRepository milestoneRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionService transactionService;

    @Transactional(readOnly = true)
    public GoalsListResponse getGoalsByUser(Long userId) {
        List<SavingsGoal> goals = goalRepository.findByUserIdOrderByPriorityAscCreatedAtDesc(userId);
        
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();
        BigDecimal thisMonthContributions = contributionRepository.sumAmountByUserIdAndDateRange(userId, startOfMonth, endOfMonth);
        
        List<SavingsGoalResponse> goalResponses = goals.stream()
                .map(goal -> {
                    Optional<GoalMilestone> nextMilestone = milestoneRepository.findNextMilestoneByGoalId(goal.getId());
                    return SavingsGoalResponse.fromEntity(goal, nextMilestone.orElse(null));
                })
                .collect(Collectors.toList());
        
        BigDecimal totalCurrent = goals.stream()
                .map(SavingsGoal::getCurrentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalTarget = goalRepository.sumTargetAmountByUserId(userId);
        if (totalTarget == null) totalTarget = BigDecimal.ZERO;
        
        Long activeGoals = goalRepository.countActiveByUserId(userId);
        Long completedGoals = goalRepository.countCompletedByUserId(userId);
        
        BigDecimal overallProgress = BigDecimal.ZERO;
        if (totalTarget.compareTo(BigDecimal.ZERO) > 0) {
            overallProgress = totalCurrent.multiply(BigDecimal.valueOf(100))
                    .divide(totalTarget, 2, RoundingMode.HALF_UP);
        }
        
        GoalsListResponse.GoalsSummary summary = GoalsListResponse.GoalsSummary.builder()
                .totalTargetAmount(totalTarget)
                .totalCurrentAmount(totalCurrent)
                .overallProgress(overallProgress)
                .activeGoals(activeGoals)
                .completedGoals(completedGoals)
                .thisMonthContributions(thisMonthContributions != null ? thisMonthContributions : BigDecimal.ZERO)
                .build();
        
        return GoalsListResponse.builder()
                .goals(goalResponses)
                .summary(summary)
                .build();
    }

    @Transactional(readOnly = true)
    public SavingsGoalResponse getGoalById(Long goalId, Long userId) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        Optional<GoalMilestone> nextMilestone = milestoneRepository.findNextMilestoneByGoalId(goalId);
        return SavingsGoalResponse.fromEntity(goal, nextMilestone.orElse(null));
    }

    @Transactional
    public SavingsGoalResponse createGoal(Long userId, CreateGoalRequest request) {
        SavingsGoal goal = SavingsGoal.builder()
                .userId(userId)
                .name(request.getName())
                .description(request.getDescription())
                .targetAmount(request.getTargetAmount())
                .currentAmount(BigDecimal.ZERO)
                .currency(request.getCurrency() != null ? request.getCurrency() : "RUB")
                .icon(request.getIcon())
                .color(request.getColor())
                .goalType(request.getGoalType() != null ? request.getGoalType() : SavingsGoal.GoalType.OTHER)
                .priority(request.getPriority() != null ? request.getPriority() : 5)
                .targetDate(request.getTargetDate())
                .deadlineEnabled(request.getDeadlineEnabled() != null ? request.getDeadlineEnabled() : false)
                .monthlyTarget(request.getMonthlyTarget())
                .autoContributionEnabled(request.getAutoContributionEnabled() != null ? request.getAutoContributionEnabled() : false)
                .autoContributionAmount(request.getAutoContributionAmount())
                .autoContributionSource(request.getAutoContributionSource())
                .autoContributionPercent(request.getAutoContributionPercent())
                .status(SavingsGoal.GoalStatus.ACTIVE)
                .build();
        
        if (request.getContributionCategoryId() != null) {
            Category category = categoryRepository.findById(request.getContributionCategoryId())
                    .orElse(null);
            goal.setContributionCategory(category);
        }
        
        goal = goalRepository.save(goal);
        return SavingsGoalResponse.fromEntity(goal);
    }

    @Transactional
    public SavingsGoalResponse updateGoal(Long goalId, Long userId, UpdateGoalRequest request) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        if (request.getName() != null) goal.setName(request.getName());
        if (request.getDescription() != null) goal.setDescription(request.getDescription());
        if (request.getTargetAmount() != null) goal.setTargetAmount(request.getTargetAmount());
        if (request.getCurrency() != null) goal.setCurrency(request.getCurrency());
        if (request.getIcon() != null) goal.setIcon(request.getIcon());
        if (request.getColor() != null) goal.setColor(request.getColor());
        if (request.getGoalType() != null) goal.setGoalType(request.getGoalType());
        if (request.getPriority() != null) goal.setPriority(request.getPriority());
        if (request.getTargetDate() != null) goal.setTargetDate(request.getTargetDate());
        if (request.getDeadlineEnabled() != null) goal.setDeadlineEnabled(request.getDeadlineEnabled());
        if (request.getMonthlyTarget() != null) goal.setMonthlyTarget(request.getMonthlyTarget());
        if (request.getAutoContributionEnabled() != null) goal.setAutoContributionEnabled(request.getAutoContributionEnabled());
        if (request.getAutoContributionAmount() != null) goal.setAutoContributionAmount(request.getAutoContributionAmount());
        if (request.getAutoContributionSource() != null) goal.setAutoContributionSource(request.getAutoContributionSource());
        if (request.getAutoContributionPercent() != null) goal.setAutoContributionPercent(request.getAutoContributionPercent());
        if (request.getContributionCategoryId() != null) {
            Category category = categoryRepository.findById(request.getContributionCategoryId())
                    .orElse(null);
            goal.setContributionCategory(category);
        }
        
        goal = goalRepository.save(goal);
        return SavingsGoalResponse.fromEntity(goal);
    }

    @Transactional
    public void deleteGoal(Long goalId, Long userId) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        goalRepository.delete(goal);
    }

    @Transactional
    public GoalContributionResponse contribute(Long goalId, Long userId, ContributeRequest request) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        if (goal.getStatus() != SavingsGoal.GoalStatus.ACTIVE) {
            throw new IllegalStateException("Нельзя пополнить неактивную цель");
        }
        
        GoalContribution.ContributionType contributionType = GoalContribution.ContributionType.MANUAL;
        if (request.getContributionType() != null) {
            try {
                contributionType = GoalContribution.ContributionType.valueOf(request.getContributionType());
            } catch (IllegalArgumentException e) {
                contributionType = GoalContribution.ContributionType.MANUAL;
            }
        }
        
        GoalContribution contribution = GoalContribution.builder()
                .goal(goal)
                .amount(request.getAmount())
                .currency(goal.getCurrency())
                .contributionType(contributionType)
                .note(request.getNote())
                .contributionDate(LocalDate.now())
                .build();
        
        contribution = contributionRepository.save(contribution);
        
        goal.setCurrentAmount(goal.getCurrentAmount().add(request.getAmount()));
        goalRepository.save(goal);
        
        return GoalContributionResponse.fromEntity(contribution);
    }

    @Transactional
    public SavingsGoalResponse pauseGoal(Long goalId, Long userId) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        goal.setStatus(SavingsGoal.GoalStatus.PAUSED);
        goal = goalRepository.save(goal);
        return SavingsGoalResponse.fromEntity(goal);
    }

    @Transactional
    public SavingsGoalResponse resumeGoal(Long goalId, Long userId) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        goal.setStatus(SavingsGoal.GoalStatus.ACTIVE);
        goal = goalRepository.save(goal);
        return SavingsGoalResponse.fromEntity(goal);
    }

    @Transactional
    public SavingsGoalResponse completeGoal(Long goalId, Long userId) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        goal.setStatus(SavingsGoal.GoalStatus.COMPLETED);
        goal.setCompletedAt(LocalDateTime.now());
        goal = goalRepository.save(goal);
        return SavingsGoalResponse.fromEntity(goal);
    }

    @Transactional(readOnly = true)
    public List<GoalContributionResponse> getContributions(Long goalId, Long userId) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        return contributionRepository.findByGoalIdOrderByContributionDateDesc(goalId)
                .stream()
                .map(GoalContributionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public GoalMilestoneResponse addMilestone(Long goalId, Long userId, CreateMilestoneRequest request) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        Integer maxOrder = milestoneRepository.findByGoalIdOrderByOrderIndexAsc(goalId)
                .stream()
                .map(GoalMilestone::getOrderIndex)
                .max(Integer::compareTo)
                .orElse(0);
        
        GoalMilestone milestone = GoalMilestone.builder()
                .goal(goal)
                .name(request.getName())
                .targetAmount(request.getTargetAmount())
                .targetDate(request.getTargetDate())
                .orderIndex(request.getOrderIndex() != null ? request.getOrderIndex() : maxOrder + 1)
                .isCompleted(false)
                .build();
        
        milestone = milestoneRepository.save(milestone);
        return GoalMilestoneResponse.fromEntity(milestone, goal.getCurrentAmount());
    }

    @Transactional(readOnly = true)
    public List<GoalMilestoneResponse> getMilestones(Long goalId, Long userId) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        return milestoneRepository.findByGoalIdOrderByOrderIndexAsc(goalId)
                .stream()
                .map(m -> GoalMilestoneResponse.fromEntity(m, goal.getCurrentAmount()))
                .collect(Collectors.toList());
    }

    @Transactional
    public GoalMilestoneResponse completeMilestone(Long milestoneId, Long goalId, Long userId) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        GoalMilestone milestone = milestoneRepository.findByIdAndGoalId(milestoneId, goalId)
                .orElseThrow(() -> new IllegalArgumentException("Веха не найдена"));
        
        milestone.setIsCompleted(true);
        milestone.setCompletedAt(LocalDateTime.now());
        milestone = milestoneRepository.save(milestone);
        
        return GoalMilestoneResponse.fromEntity(milestone, goal.getCurrentAmount());
    }

    @Transactional
    public void deleteMilestone(Long milestoneId, Long goalId, Long userId) {
        SavingsGoal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Цель не найдена"));
        
        GoalMilestone milestone = milestoneRepository.findByIdAndGoalId(milestoneId, goalId)
                .orElseThrow(() -> new IllegalArgumentException("Веха не найдена"));
        
        milestoneRepository.delete(milestone);
    }
}
