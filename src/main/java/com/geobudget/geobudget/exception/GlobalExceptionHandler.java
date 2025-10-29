package com.geobudget.geobudget.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.geobudget.geobudget.exception.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error occured: {}", ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse response = new ErrorResponse("Ошибка валидации", "Проверьте введенные данные");
        response.setErrorsList(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation error occured: {}", ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        ErrorResponse response = new ErrorResponse("Validation error", "Check input data");
        response.setErrorsList(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<ErrorResponse> handleDataValidationException(DataValidationException ex) {
        log.error("Data validation exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(ex.getMessage(), "Data validation error, check input data"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("Entity not found exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(ex.getMessage(), "Entity not found, check id"));
    }

    @ExceptionHandler(EntityWasRemovedException.class)
    public ResponseEntity<ErrorResponse> handleEntityWasRemovedException(EntityWasRemovedException ex) {
        log.error("Entity was removed exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), "Entity is no longer available"));
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("Json processing exception: {}", ex.getMessage(), ex);
        String errorMessage = extractMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(errorMessage, "Json processing error"));
    }

    @ExceptionHandler(NetworkException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleNetworkException(NetworkException ex) {
        log.error("Network error occurred: {}", ex.getMessage(), ex);

        String fullMessage = ex.getMessage();
        int lastBracketIndex = fullMessage.lastIndexOf("[");

        if (lastBracketIndex != -1 && lastBracketIndex + 1 < fullMessage.length()) {
            return fullMessage.substring(lastBracketIndex + 1, fullMessage.length() - 1);
        }

        return "Unknown error";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage(), ex);

        String fullMessage = ex.getMessage();
        int lastBracketIndex = fullMessage.lastIndexOf("[");

        if (lastBracketIndex != -1 && lastBracketIndex + 1 < fullMessage.length()) {
            return fullMessage.substring(lastBracketIndex + 1, fullMessage.length() - 1);
        }

        return "User not found";
    }

    private String extractMessage(String fullMessage) {
        int lastBracketIndex = fullMessage.lastIndexOf("[");
        if (lastBracketIndex != -1 && lastBracketIndex + 1 < fullMessage.length()) {
            return fullMessage.substring(lastBracketIndex + 1, fullMessage.length() - 1);
        }
        return "Unknown error";
    }

    @ExceptionHandler(NotUniqueAlbumException.class)
    public ResponseEntity<String> handleNotUniqueAlbumException(NotUniqueAlbumException ex) {
        log.error("NotUniqueAlbumException access exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException access exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(ex.getMessage(), "Проверьте введенные данные"));
    }

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<ErrorResponse> handleMismatchedInputException(MismatchedInputException ex) {
        log.error("MismatchedInputException access exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse("Пользователь не авторизован", "Для доступа к запрашиваемому ресурсу требуется аутентификация."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException access exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse("Ошибка запроса", "Проверьте введенные данные"));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        log.error("CategoryNotFoundException access exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse("Не верная категория", "Категория не найдена. Проверьте введенные данные"));
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        log.error("JwtAuthenticationException access exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse("Ошибка аутентификации", ex.getMessage()));
    }

    @ExceptionHandler(ReceiptExisting.class)
    public ResponseEntity<ErrorResponse> handleJwtAuthenticationException(ReceiptExisting ex) {
        log.error("JwtAuthenticationException access exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ErrorResponse("Такой чек уже добавлен", ex.getMessage()));
    }
}