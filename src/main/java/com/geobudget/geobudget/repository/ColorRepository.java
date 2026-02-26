package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Color;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends CrudRepository<Color, Long> {
    @Query("SELECT c FROM Color c WHERE c.group.id = :groupId")
    List<Color> findByGroupId(@Param("groupId") Long groupId);
}
