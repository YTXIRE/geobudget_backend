package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Icon;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IconRepository extends CrudRepository<Icon, Long> {
    @Query("SELECT i FROM Icon i WHERE i.group.id = :groupId")
    List<Icon> findByGroupId(@Param("groupId") Long groupId);
}
