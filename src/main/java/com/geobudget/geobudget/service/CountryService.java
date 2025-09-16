package com.geobudget.geobudget.service;

import com.geobudget.geobudget.dto.Country;
import com.geobudget.geobudget.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<Country> getCountries() {
        List<Country> countries = new ArrayList<>();
        countryRepository.findAll().forEach(country -> countries.add(Country.builder().id(country.getId()).title(country.getTitle()).build()));
        return countries;
    }
}
