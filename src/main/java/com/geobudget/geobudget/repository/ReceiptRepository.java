package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Receipt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
    Optional<Receipt> findByTimestampAndInnAndTotalSum(Long timestamp, String inn, double totalSum);

    @Query("SELECT COUNT(r) FROM Receipt r WHERE r.category.id = :categoryId")
    Integer countByCategoryId(Long categoryId);

    @Query("SELECT COALESCE(SUM(r.totalSum), 0) FROM Receipt r WHERE r.category.id = :categoryId")
    Double sumTotalSumByCategoryId(Long categoryId);
}
