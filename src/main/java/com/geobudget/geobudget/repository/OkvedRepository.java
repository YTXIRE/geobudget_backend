package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Okved;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OkvedRepository extends CrudRepository<Okved, Long> {
    Optional<Okved> findByCode(String code);
}
