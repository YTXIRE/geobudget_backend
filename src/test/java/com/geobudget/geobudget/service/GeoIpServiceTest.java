package com.geobudget.geobudget.service;

import com.geobudget.geobudget.validator.GeoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

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

    @Test
    void getCityByIp_returnsCity() {
        when(restTemplate.getForObject(anyString(), eq(String.class), anyString()))
                .thenReturn("{\"city\":\"Москва\"}");
        String city = service.getCityByIp("8.8.8.8");
        assertEquals("Москва", city);
    }

    @Test
    void getExternalIp_returnsValue() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenReturn("1.2.3.4");
        String ip = service.getExternalIp();
        assertEquals("1.2.3.4", ip);
    }

    @Test
    void getCityByIp_handlesNullResponseGracefully() {
        when(restTemplate.getForObject(anyString(), eq(String.class), anyString()))
                .thenReturn(null);
        String city = service.getCityByIp("8.8.8.8");
        assertNull(city);
    }
}


