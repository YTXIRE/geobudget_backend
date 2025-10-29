package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Category;
import com.geobudget.geobudget.entity.Okved;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OkvedRepositoryTest {

    @Autowired
    private OkvedRepository okvedRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByCode_returnsSavedEntity() {
        Category category = new Category();
        category.setName("IT");
        category.setDescription("Информационные технологии");
        category = categoryRepository.save(category);

        Okved okved = new Okved();
        okved.setCode("62.01");
        okved.setName("Разработка ПО");
        okved.setCategory(category);
        okvedRepository.save(okved);

        assertTrue(okvedRepository.findByCode("62.01").isPresent());
    }

    @Test
    void findByCode_returnsEmptyForUnknown() {
        assertTrue(okvedRepository.findByCode("00.00").isEmpty());
    }
}


