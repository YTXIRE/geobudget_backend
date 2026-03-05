package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.icon LEFT JOIN FETCH c.color LEFT JOIN FETCH c.group WHERE c.type = 'system' ORDER BY CASE WHEN c.isFavorite = true THEN 0 ELSE 1 END, c.id")
    List<Category> findAllSystemCategories();

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.icon LEFT JOIN FETCH c.color LEFT JOIN FETCH c.group WHERE c.userId = :userId ORDER BY CASE WHEN c.isFavorite = true THEN 0 ELSE 1 END, c.id")
    List<Category> findUserCategoriesByUserId(Long userId);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.icon LEFT JOIN FETCH c.color LEFT JOIN FETCH c.group WHERE c.id = :id")
    Optional<Category> findByIdWithIconAndColor(Long id);
}
