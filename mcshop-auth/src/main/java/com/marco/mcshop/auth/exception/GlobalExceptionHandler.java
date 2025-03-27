package com.marco.mcshop.auth.exception;

import com.marco.mcshop.auth.exception.auth.OtpValidationFailedException;
import com.marco.mcshop.auth.exception.auth.RefreshTokenInvalidException;
import com.marco.mcshop.auth.exception.customer.CustomerAlreadyExistException;
import com.marco.mcshop.auth.exception.customer.CustomerNotFoundException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({CustomerNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(Exception exception) {
        log.error(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler({CustomerAlreadyExistException.class})
    public ResponseEntity<ErrorResponse> handleCustomerAlreadyExistsException(CustomerAlreadyExistException exception) {
        log.error(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler({OtpValidationFailedException.class})
    public ResponseEntity<ErrorResponse> handleOtpValidationFailedException(OtpValidationFailedException exception) {
        log.error(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler({RefreshTokenInvalidException.class, JwtException.class})
    public ResponseEntity<ErrorResponse> handleRefreshTokenInvalidException(Exception exception) {
        log.error(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder().message(exception.getMessage()).build());
    }

    // unexpected exception
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleUnexpectedException(RuntimeException exception) {
        log.error(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().message(exception.getMessage()).build());
    }
}

