package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.docs.companyInfo.GetCompanyByInnDoc;
import com.geobudget.geobudget.dto.Country;
import com.geobudget.geobudget.service.CountryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/country")
@Tag(name = "Country", description = "Работа со странами")
@Validated
public class CountryController {
    private final CountryService countryService;

    @GetCompanyByInnDoc
    @GetMapping
    public ResponseEntity<List<Country>> getCountry() {
             return ResponseEntity.ok(countryService.getCountries());
    }
}
