package com.geobudget.geobudget.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.geobudget.geobudget.exception.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + " " + fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Validation failed", details);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Validation failed", details);
    }

    @ExceptionHandler({IllegalArgumentException.class, DataValidationException.class, HttpMessageNotReadableException.class, MismatchedInputException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, "Validation failed", List.of(ex.getMessage()));
    }

    @ExceptionHandler({JwtAuthenticationException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(Exception ex) {
        return build(HttpStatus.UNAUTHORIZED, "Unauthorized", List.of(ex.getMessage()));
    }

    @ExceptionHandler({AccessDeniedException.class, UnauthorizedAccessException.class})
    public ResponseEntity<ErrorResponse> handleForbidden(Exception ex) {
        return build(HttpStatus.FORBIDDEN, "Access denied", List.of(ex.getMessage()));
    }

    @ExceptionHandler({EntityNotFoundException.class, UserNotFoundException.class, CategoryNotFoundException.class, EntityWasRemovedException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), List.of(ex.getMessage()));
    }

    @ExceptionHandler({NotUniqueAlbumException.class, ReceiptExisting.class})
    public ResponseEntity<ErrorResponse> handleConflict(Exception ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), List.of(ex.getMessage()));
    }

    @ExceptionHandler({JsonProcessingException.class, NetworkException.class})
    public ResponseEntity<ErrorResponse> handleExternalErrors(Exception ex) {
        return build(HttpStatus.BAD_GATEWAY, ex.getMessage(), List.of(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnhandled(Exception ex) {
        log.error("Unhandled exception", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", List.of("Unexpected error"));
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, List<String> details) {
        List<String> normalizedDetails = details == null ? new ArrayList<>() : details;
        ErrorResponse response = ErrorResponse.builder()
                .statusCode(status.value())
                .message(message)
                .details(normalizedDetails)
                .build();

        return ResponseEntity.status(status).body(response);
    }
}
