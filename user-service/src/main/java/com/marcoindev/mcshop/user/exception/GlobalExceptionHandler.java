package com.marcoindev.mcshop.user.exception;

import com.marcoindev.mcshop.common.payload.ErrorResponse;
import com.marcoindev.mcshop.user.exception.profile.ProfileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({
            ProfileNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleCustomException(RuntimeException ex, WebRequest request) {
        log.error("Custom exception handler: ", ex);
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    // Validation exception:
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Validation exception handler: ", ex);

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining());

        return buildErrorResponse(new Exception("Validation failed, " + details), HttpStatus.BAD_REQUEST, request);
    }

    // all exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Unexpected exception handler: ", ex);

        return buildErrorResponse(
                new Exception("Internal server error"),  // Generic message for clients
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(status)
                .body(ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .error(ex.getMessage())
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build());
    }
}