package com.marcoindev.mcshop.auth.exception;

import com.marcoindev.mcshop.auth.exception.auth.OtpValidationFailedException;
import com.marcoindev.mcshop.auth.exception.auth.RefreshTokenInvalidException;
import com.marcoindev.mcshop.auth.exception.user.UserAlreadyExistException;
import com.marcoindev.mcshop.auth.exception.user.UserNotFoundException;
import com.marcoindev.mcshop.common.payload.ErrorResponse;
import io.jsonwebtoken.JwtException;
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
    // custom exception:
    @ExceptionHandler({
            UserNotFoundException.class,
            UserAlreadyExistException.class,
            RefreshTokenInvalidException.class,
            OtpValidationFailedException.class
    })
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(RuntimeException ex, WebRequest request) {
        log.error("Custom exception handler: ", ex);
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    // JWT exception:
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJWTException(JwtException ex, WebRequest request) {
        log.error("JwtException exception handler: ", ex);

        String message = switch (ex.getClass().getSimpleName()) {
            case "SignatureException" -> "JWT signature does not match";
            default -> "JWT is invalid";
        };
        return buildErrorResponse(new Exception(message), HttpStatus.UNAUTHORIZED, request);
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