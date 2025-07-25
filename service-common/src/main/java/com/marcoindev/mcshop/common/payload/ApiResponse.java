package com.marcoindev.mcshop.common.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class ApiResponse<T> {
    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @Builder.Default
    private int code = 200;
    
    private T data;

    // Success response builders
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(ErrorCode.SUCCESS.getCode())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .code(ErrorCode.CREATED.getCode())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> updated(T data) {
        return ApiResponse.<T>builder()
                .code(ErrorCode.UPDATED.getCode())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> deleted() {
        return ApiResponse.<T>builder()
                .code(ErrorCode.DELETED.getCode())
                .build();
    }
}
