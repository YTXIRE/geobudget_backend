package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndUserIdAndIsDeletedFalse(Long id, Long userId);

    @Query("""
            SELECT
                COALESCE(SUM(CASE WHEN t.type = 'income' THEN t.amount ELSE 0 END), 0) AS income,
                COALESCE(SUM(CASE WHEN t.type = 'expense' THEN t.amount ELSE 0 END), 0) AS expense
            FROM Transaction t
            WHERE t.userId = :userId
              AND t.isDeleted = false
              AND t.occurredAt >= COALESCE(:from, t.occurredAt)
              AND t.occurredAt <= COALESCE(:to, t.occurredAt)
            """)
    TransactionSummaryProjection getSummary(Long userId, LocalDateTime from, LocalDateTime to);
}
