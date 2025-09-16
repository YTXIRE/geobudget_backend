package com.geobudget.geobudget.validator;

import com.geobudget.geobudget.exception.DataValidationException;
import org.springframework.stereotype.Component;

@Component
public class ReceiptValidator {
    public void validateQr(String qrString) {
        if (qrString == null || qrString.isBlank()) {
            throw new DataValidationException("qr обязателен");
        }
        // Дополнительно можно проверить формат, длину и допустимые символы
        // if (!qrString.matches("^[A-Za-z0-9:=._-]+$")) {
        //     throw new DataValidationException("Некорректный формат qr");
        // }
    }
}


