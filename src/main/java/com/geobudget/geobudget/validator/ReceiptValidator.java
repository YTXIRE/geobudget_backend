package com.geobudget.geobudget.validator;

import com.geobudget.geobudget.exception.DataValidationException;
import org.springframework.stereotype.Component;

@Component
public class ReceiptValidator {
    public void validateQr(String qrString) {
        if (qrString == null || qrString.isBlank()) {
            throw new DataValidationException("qr обязателен");
        }

        String[] qrList = qrString.split("&");

        if (qrList.length != 6 || qrList[0].startsWith("t=") || qrList[1].startsWith("s=") || qrList[2].startsWith("fn=") || qrList[3].startsWith("i=") || qrList[4].startsWith("fp=") || qrList[5].startsWith("n=")) {
            throw new DataValidationException("Некорректный формат qr");
        }

        // Дополнительно можно проверить формат, длину и допустимые символы
        // if (!qrString.matches("^[A-Za-z0-9:=._-]+$")) {
        //     throw new DataValidationException("Некорректный формат qr");
        // }
    }
}


