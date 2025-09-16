package com.geobudget.geobudget.validator;

import com.geobudget.geobudget.exception.DataValidationException;
import org.springframework.stereotype.Component;

@Component
public class GeoValidator {
    public void validateIp(String ip) {
        if (ip == null || ip.isBlank()) {
            throw new DataValidationException("IP обязателен");
        }
        // Базовая проверка IPv4; при необходимости расширить IPv6
        if (!ip.matches("^(25[0-5]|2[0-4]\\d|1?\\d{1,2})(\\.(25[0-5]|2[0-4]\\d|1?\\d{1,2})){3}$")) {
            throw new DataValidationException("Некорректный формат IP");
        }
    }

    public void validateAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new DataValidationException("Адрес обязателен");
        }
    }
}


