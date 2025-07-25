package com.marcoindev.mcshop.common.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class ErrorResponse {
    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private int code;
    
    private String error;
    
    private String path;
    
    private String traceId;

    // Static builders for common error responses
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .error(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, String customMessage) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .error(customMessage)
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, String customMessage, String path) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .error(customMessage)
                .path(path)
                .build();
    }

    public static ErrorResponse of(int code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .error(message)
                .build();
    }

    public static ErrorResponse of(int code, String message, String path) {
        return ErrorResponse.builder()
                .code(code)
                .error(message)
                .path(path)
                .build();
    }
}
