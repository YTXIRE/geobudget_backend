package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.ReceiptCategoryPreference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptCategoryPreferenceRepository extends CrudRepository<ReceiptCategoryPreference, Long> {
    Optional<ReceiptCategoryPreference> findByUserIdAndInn(Long userId, String inn);
}
