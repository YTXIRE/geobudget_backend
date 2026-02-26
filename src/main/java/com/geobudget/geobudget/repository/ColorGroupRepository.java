package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.ColorGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorGroupRepository extends CrudRepository<ColorGroup, Long> {
}
