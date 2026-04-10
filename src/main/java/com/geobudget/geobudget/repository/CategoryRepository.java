package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query("""
            SELECT c
            FROM Category c
            LEFT JOIN FETCH c.icon
            LEFT JOIN FETCH c.color
            LEFT JOIN FETCH c.group
            WHERE (c.type = 'system' OR c.userId = :userId)
              AND (:transactionType IS NULL OR c.transactionType = :transactionType)
              AND (:includeArchived = true OR COALESCE(c.isArchived, false) = false)
            ORDER BY CASE WHEN COALESCE(c.isFavorite, false) = true THEN 0 ELSE 1 END, c.id
            """)
    List<Category> findCategoriesForUser(Long userId, String transactionType, boolean includeArchived);

    @Query("""
            SELECT c
            FROM Category c
            LEFT JOIN FETCH c.icon
            LEFT JOIN FETCH c.color
            LEFT JOIN FETCH c.group
            WHERE c.id = :id
              AND (c.type = 'system' OR c.userId = :userId)
            """)
    Optional<Category> findAccessibleById(Long id, Long userId);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.icon LEFT JOIN FETCH c.color LEFT JOIN FETCH c.group WHERE c.id = :id")
    Optional<Category> findByIdWithIconAndColor(Long id);

    @Modifying
    @Query("UPDATE Category c SET c.group = NULL WHERE c.group.id = :groupId")
    void clearGroupIdByGroupId(Long groupId);

    List<Category> findByGroupId(Long groupId);

    @Query("SELECT c.name FROM Category c WHERE c.group.id = :groupId")
    List<String> findNamesByGroupId(Long groupId);
}
