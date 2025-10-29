package com.geobudget.geobudget.validator;

import com.geobudget.geobudget.exception.DataValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoValidatorTest {
    private final GeoValidator validator = new GeoValidator();

    @Test
    void validateIp_acceptsValidIpv4() {
        assertDoesNotThrow(() -> validator.validateIp("8.8.8.8"));
        assertDoesNotThrow(() -> validator.validateIp("192.168.1.1"));
    }

    @Test
    void validateIp_rejectsBlank() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateIp(""));
        assertTrue(ex.getMessage().contains("IP обязателен"));
    }

    @Test
    void validateIp_rejectsInvalid() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateIp("999.0.0.1"));
        assertTrue(ex.getMessage().contains("Некорректный формат IP"));
    }

    @Test
    void validateIp_rejectsNull() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateIp(null));
        assertTrue(ex.getMessage().contains("IP обязателен"));
    }

    @Test
    void validateAddress_acceptsNonBlank() {
        assertDoesNotThrow(() -> validator.validateAddress("Москва, Тверская 1"));
    }

    @Test
    void validateAddress_rejectsBlank() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateAddress(" "));
        assertTrue(ex.getMessage().contains("Адрес обязателен"));
    }

    @Test
    void validateAddress_rejectsNull() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateAddress(null));
        assertTrue(ex.getMessage().contains("Адрес обязателен"));
    }
}


