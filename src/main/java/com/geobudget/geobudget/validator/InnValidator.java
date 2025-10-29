package com.geobudget.geobudget.validator;

import com.geobudget.geobudget.exception.DataValidationException;
import org.springframework.stereotype.Component;

@Component
public class InnValidator {
    public void validateInn(String inn) {
        if (inn == null || inn.isBlank()) {
            throw new DataValidationException("ИНН обязателен");
        }
        if (!inn.matches("^\\d{10}|\\d{12}$")) {
            throw new DataValidationException("ИНН должен состоять из 10 или 12 цифр");
        }
    }
}


