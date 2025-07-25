package com.marcoindev.mcshop.order.exception;

import com.marcoindev.mcshop.common.payload.ErrorCode;
import com.marcoindev.mcshop.common.payload.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    // API runtime exceptions with error codes
    @ExceptionHandler(APIRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleAPIRuntimeException(APIRuntimeException ex, WebRequest request) {
        log.error("API runtime exception handler: ", ex);
        
        String path = request.getDescription(false).replace("uri=", "");
        
        // Use the error code from the exception
        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                path
        );
        
        // Map error codes to appropriate HTTP status codes
        HttpStatus httpStatus = mapErrorCodeToHttpStatus(ex.getErrorCode());
        
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // Validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Validation exception handler: ", ex);

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        String path = request.getDescription(false).replace("uri=", "");
        
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.VALIDATION_ERROR,
                "Validation failed: " + details,
                path
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // All unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Unexpected exception handler: ", ex);

        String path = request.getDescription(false).replace("uri=", "");
        
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.SYSTEM_ERROR,
                "Internal server error",
                path
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Maps custom error codes to appropriate HTTP status codes
     */
    private HttpStatus mapErrorCodeToHttpStatus(int errorCode) {
        if (errorCode >= 200 && errorCode < 300) {
            return HttpStatus.OK;
        } else if (errorCode >= 1000 && errorCode < 1100) {
            // Purchase/Order errors - mostly client errors
            return HttpStatus.BAD_REQUEST;
        } else if (errorCode >= 2000 && errorCode < 2100) {
            // Auth errors - mostly unauthorized
            return HttpStatus.UNAUTHORIZED;
        } else if (errorCode >= 3000 && errorCode < 3100) {
            // User errors - mostly not found or bad request
            return HttpStatus.BAD_REQUEST;
        } else if (errorCode >= 4000 && errorCode < 4100) {
            // Validation errors
            return HttpStatus.BAD_REQUEST;
        } else if (errorCode >= 500 && errorCode < 600) {
            // System errors
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (errorCode >= 6000 && errorCode < 6100) {
            // Resource errors - mostly not found
            return HttpStatus.NOT_FOUND;
        } else {
            // Default to internal server error
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}