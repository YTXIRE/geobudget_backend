package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Partner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends CrudRepository<Partner, Long> {
    List<Partner> findByUserIdAndStatus(Long userId, String status);

    List<Partner> findByUserIdAndStatusIn(Long userId, List<String> statuses);

    @Query("SELECT p FROM Partner p WHERE p.userId = :userId AND p.status = 'accepted'")
    List<Partner> findAcceptedPartners(Long userId);

    @Query("SELECT p FROM Partner p WHERE p.userId = :userId AND p.status = 'pending'")
    List<Partner> findPendingInvitations(Long userId);

    @Query("SELECT p FROM Partner p WHERE p.partnerId = :userId AND p.status = 'pending'")
    List<Partner> findPendingIncoming(Long userId);

    Optional<Partner> findByUserIdAndPartnerId(Long userId, Long partnerId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Partner p " +
           "WHERE ((p.userId = :userId1 AND p.partnerId = :userId2) " +
           "OR (p.userId = :userId2 AND p.partnerId = :userId1)) " +
           "AND p.status = 'accepted'")
    boolean existsAcceptedPartnership(Long userId1, Long userId2);

    @Query("SELECT p FROM Partner p WHERE " +
           "(p.userId = :userId OR p.partnerId = :userId) " +
           "AND p.status = 'accepted'")
    List<Partner> findAllAcceptedForUser(Long userId);
}
