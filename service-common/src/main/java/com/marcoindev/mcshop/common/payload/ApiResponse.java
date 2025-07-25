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
}
