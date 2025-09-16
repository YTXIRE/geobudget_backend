package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.dto.geoCompany.AddressDto;
import com.geobudget.geobudget.service.DadataService;
import com.geobudget.geobudget.service.GeoIpService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GeoController.class)
class GeoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GeoIpService geoIpService;

    @Autowired
    private DadataService dadataService;

    @Test
    @WithMockUser(roles = {"USER"})
    void getCity_returns200() throws Exception {
        Mockito.when(geoIpService.getCityByIp(anyString())).thenReturn("Moscow");

        mockMvc.perform(get("/api/v1/geo/get-city").param("ip", "8.8.8.8"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getIp_returns200() throws Exception {
        Mockito.when(geoIpService.getExternalIp()).thenReturn("1.2.3.4");

        mockMvc.perform(get("/api/v1/geo/get-ip"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getGeoCompany_returns200() throws Exception {
        Mockito.when(dadataService.getGeoCompany(anyString())).thenReturn(new AddressDto());

        mockMvc.perform(get("/api/v1/geo/get-geo-company").param("address", "Москва"))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        GeoIpService geoIpService() {
            return Mockito.mock(GeoIpService.class);
        }

        @Bean
        DadataService dadataService() {
            return Mockito.mock(DadataService.class);
        }
    }
}
