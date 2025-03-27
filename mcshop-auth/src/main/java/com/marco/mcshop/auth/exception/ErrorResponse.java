package com.marco.mcshop.auth.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class ErrorResponse {
    private String message;
    private String traceId;
}
