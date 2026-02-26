package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.icon LEFT JOIN FETCH c.color ORDER BY CASE WHEN c.isFavorite = true THEN 0 ELSE 1 END, c.id")
    List<Category> findAllWithIconAndColor();
}
