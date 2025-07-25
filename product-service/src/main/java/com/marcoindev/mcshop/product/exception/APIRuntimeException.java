package com.marcoindev.mcshop.product.exception;

import com.marcoindev.mcshop.common.payload.ErrorCode;

public class APIRuntimeException extends RuntimeException {
    private final int errorCode;
    private final ErrorCode errorCodeEnum;

    // Constructor with ErrorCode enum
    public APIRuntimeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
        this.errorCodeEnum = errorCode;
    }

    // Constructor with ErrorCode enum and custom message
    public APIRuntimeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode.getCode();
        this.errorCodeEnum = errorCode;
    }

    // Constructor with ErrorCode enum, custom message, and cause
    public APIRuntimeException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode.getCode();
        this.errorCodeEnum = errorCode;
    }

    // Constructor with custom error code and message
    public APIRuntimeException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorCodeEnum = ErrorCode.fromCode(errorCode);
    }

    // Constructor with custom error code, message, and cause
    public APIRuntimeException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorCodeEnum = ErrorCode.fromCode(errorCode);
    }

    // Legacy constructor for backward compatibility
    public APIRuntimeException(String message) {
        super(message);
        this.errorCode = ErrorCode.SYSTEM_ERROR.getCode();
        this.errorCodeEnum = ErrorCode.SYSTEM_ERROR;
    }

    // Legacy constructor with cause for backward compatibility
    public APIRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.SYSTEM_ERROR.getCode();
        this.errorCodeEnum = ErrorCode.SYSTEM_ERROR;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ErrorCode getErrorCodeEnum() {
        return errorCodeEnum;
    }
}
