package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindById() {
        Category c = new Category();
        c.setName("Прочее");
        c.setDescription("Fallback");
        Category saved = categoryRepository.save(c);

        Category found = categoryRepository.findById(saved.getId().intValue());
        assertNotNull(found);
        assertEquals("Прочее", found.getName());
    }

    @Test
    void findById_returnsNullWhenNotExists() {
        Category found = categoryRepository.findById(999999);
        assertNull(found);
    }
}


