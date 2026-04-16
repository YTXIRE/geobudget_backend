package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserIdOrderByNextPaymentDateAscCreatedAtDesc(Long userId);

    Optional<Subscription> findByIdAndUserId(Long id, Long userId);
}
