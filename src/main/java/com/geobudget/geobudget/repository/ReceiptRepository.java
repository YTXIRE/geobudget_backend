package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Receipt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
    Optional<Receipt> findByTimestampAndInnAndTotalSum(Long timestamp, String inn, double totalSum);
}
