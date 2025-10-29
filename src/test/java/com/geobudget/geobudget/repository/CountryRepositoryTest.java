package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void save_and_find() {
        Country c = new Country();
        c.setTitle("Россия");
        Country saved = countryRepository.save(c);
        assertTrue(countryRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void findById_emptyWhenMissing() {
        assertTrue(countryRepository.findById(999999L).isEmpty());
    }
}


