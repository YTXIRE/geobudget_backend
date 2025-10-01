package com.geobudget.geobudget.service;

import com.geobudget.geobudget.validator.GeoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

class GeoIpServiceTest {
    private RestTemplate restTemplate;
    private GeoValidator geoValidator;
    private GeoIpService service;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        geoValidator = new GeoValidator();
        service = new GeoIpService(restTemplate, geoValidator);
    }
}


