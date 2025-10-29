package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.Country;
import com.geobudget.geobudget.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void findByEmail_returnsSavedUser() {
        Country country = new Country();
        country.setTitle("Россия");
        country = countryRepository.save(country);

        User user = new User();
        user.setUsername("john");
        user.setPassword("secret");
        user.setEmail("john@example.com");
        user.setCountry(country);
        user = userRepository.save(user);

        assertTrue(userRepository.findByEmail("john@example.com").isPresent());
        assertTrue(userRepository.findByUsername("john").isPresent());
    }

    @Test
    void findByEmail_returnsEmptyWhenNotExists() {
        assertTrue(userRepository.findByEmail("unknown@example.com").isEmpty());
        assertTrue(userRepository.findByUsername("unknown").isEmpty());
        assertTrue(userRepository.findByPhone("+70000000000").isEmpty());
    }
}


