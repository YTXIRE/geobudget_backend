package com.geobudget.geobudget.validator;

import com.geobudget.geobudget.exception.DataValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptValidatorTest {
    private final ReceiptValidator validator = new ReceiptValidator();

    @Test
    void validateQr_acceptsNonBlank() {
        assertDoesNotThrow(() -> validator.validateQr("t=20250101T1200&s=100.00"));
    }

    @Test
    void validateQr_rejectsBlank() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateQr(""));
        assertTrue(ex.getMessage().contains("qr обязателен"));
    }

    @Test
    void validateQr_rejectsNull() {
        DataValidationException ex = assertThrows(DataValidationException.class, () -> validator.validateQr(null));
        assertTrue(ex.getMessage().contains("qr обязателен"));
    }
}


