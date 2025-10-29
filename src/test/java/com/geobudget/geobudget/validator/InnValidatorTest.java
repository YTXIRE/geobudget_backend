package com.geobudget.geobudget.validator;

import com.geobudget.geobudget.exception.DataValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InnValidatorTest {
    private final InnValidator validator = new InnValidator();

    @Test
    void validateInn_accepts10Digits() {
        assertDoesNotThrow(() -> validator.validateInn("1234567890"));
    }

    @Test
    void validateInn_accepts12Digits() {
        assertDoesNotThrow(() -> validator.validateInn("123456789012"));
    }

    @Test
    void validateInn_rejectsBlank() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateInn(" "));
        assertTrue(ex.getMessage().contains("ИНН обязателен"));
    }

    @Test
    void validateInn_rejectsWrongLength() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateInn("12345"));
        assertTrue(ex.getMessage().contains("ИНН должен состоять"));
    }

    @Test
    void validateInn_rejectsNonDigits() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateInn("12345abcde"));
        assertTrue(ex.getMessage().contains("ИНН должен состоять"));
    }

    @Test
    void validateInn_rejectsNull() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateInn(null));
        assertTrue(ex.getMessage().contains("ИНН обязателен"));
    }
}


