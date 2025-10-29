package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category findById(Integer id);
}
