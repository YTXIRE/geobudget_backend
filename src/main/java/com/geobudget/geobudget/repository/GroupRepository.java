package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long> {
    List<Group> findByUserIdIsNullOrUserId(Long userId);

    @Query("SELECT g FROM Group g WHERE g.id = :id AND (g.userId IS NULL OR g.userId = :userId)")
    Optional<Group> findByIdAndUserIdIsNullOrUserId(Long id, Long userId);
    
    boolean existsByNameAndUserId(String name, Long userId);
    
    boolean existsByNameAndUserIdIsNull(String name);
}
