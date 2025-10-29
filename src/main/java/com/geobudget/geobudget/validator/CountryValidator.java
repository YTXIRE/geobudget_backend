package com.geobudget.geobudget.validator;

import com.geobudget.geobudget.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountryValidator {
    private final CountryRepository countryRepository;

    public void checkCountryExistence(Long country_id) {
        System.out.println("country_id = " + country_id);
        if (country_id == null || countryRepository.findById(country_id).isEmpty()) {
            log.info("Страна c id " + country_id + " не найдена");
            throw new IllegalArgumentException("Страна не найдена");
        }
    }
}
