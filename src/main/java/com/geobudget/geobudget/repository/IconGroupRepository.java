package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.IconGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IconGroupRepository extends CrudRepository<IconGroup, Long> {
}
