package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.TagTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagTransactionRepository extends JpaRepository<TagTransaction, Long> {
    
    List<TagTransaction> findByTransactionId(Long transactionId);
    
    List<TagTransaction> findByTransactionIdAndUserId(Long transactionId, Long userId);

    List<TagTransaction> findByUserIdAndTransactionIdIn(Long userId, List<Long> transactionIds);

    void deleteByTagIdAndUserId(Long tagId, Long userId);

    void deleteByTransactionId(Long transactionId);
    
    void deleteByTransactionIdAndUserId(Long transactionId, Long userId);
}
