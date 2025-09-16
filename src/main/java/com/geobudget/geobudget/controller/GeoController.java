package com.geobudget.geobudget.controller;

import com.geobudget.geobudget.docs.geo.GetCityByIpDoc;
import com.geobudget.geobudget.docs.geo.GetCityByIpExternalIpDoc;
import com.geobudget.geobudget.docs.geo.GetGeoCompany;
import com.geobudget.geobudget.docs.geo.GetIpDoc;
import com.geobudget.geobudget.dto.geoCompany.AddressDto;
import com.geobudget.geobudget.service.DadataService;
import com.geobudget.geobudget.service.GeoIpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;

@Slf4j
@Validated
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/v1/geo")
@RequiredArgsConstructor
@Tag(name = "Geo", description = "Работа с геолокацией")
public class GeoController {
    private final GeoIpService geoIpService;
    private final DadataService dadataService;

    @GetCityByIpDoc
    @GetMapping("/get-city")
    public String getCity(@RequestParam @NotBlank(message = "IP обязателен") String ip) {
        return geoIpService.getCityByIp(ip);
    }

    @GetIpDoc
    @GetMapping("/get-ip")
    public String getIp() {
        return geoIpService.getExternalIp();
    }

    @GetCityByIpExternalIpDoc
    @GetMapping("/get-city-by-external-ip")
    public String getExternalIp() {
        return geoIpService.getCityByExternalIp();
    }

    @GetGeoCompany
    @GetMapping("/get-geo-company")
    public AddressDto getGeoCompany(@RequestParam @NotBlank(message = "Адрес обязателен") String address) throws Exception {
        return dadataService.getGeoCompany(address);
    }
}
