package com.marcoindev.mcshop.common.payload;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Success Codes (200-299)
    SUCCESS(200, "Success"),
    CREATED(201, "Created successfully"),
    UPDATED(202, "Updated successfully"),
    DELETED(203, "Deleted successfully"),
    
    // System Errors (500-599)
    SYSTEM_ERROR(500, "System error occurred"),
    INTERNAL_SERVER_ERROR(501, "Internal server error"),
    SERVICE_UNAVAILABLE(502, "Service temporarily unavailable"),
    DATABASE_ERROR(503, "Database operation failed"),
    EXTERNAL_SERVICE_ERROR(504, "External service error"),
    
    // Purchase/Order Errors (1000-1099)
    PURCHASE_INSUFFICIENT_INVENTORY(1000, "Insufficient inventory"),
    PURCHASE_PRODUCT_NOT_FOUND(1001, "Product not found"),
    PURCHASE_PAYMENT_FAILED(1005, "Payment failed"),
    PURCHASE_SYSTEM_BUSY(1009, "System busy, try again"),
    
    // Auth Errors (2000-2099)
    AUTH_INVALID_CREDENTIALS(2000, "Invalid credentials"),
    AUTH_TOKEN_EXPIRED(2001, "Access token expired"),
    AUTH_TOKEN_INVALID(2002, "Invalid access token"),
    AUTH_REFRESH_TOKEN_INVALID(2003, "Invalid refresh token"),
    AUTH_REFRESH_TOKEN_EXPIRED(2004, "Refresh token expired"),
    AUTH_USER_NOT_FOUND(2005, "User not found"),
    AUTH_USER_ALREADY_EXISTS(2006, "User already exists"),
    AUTH_OTP_INVALID(2007, "Invalid OTP"),
    AUTH_OTP_EXPIRED(2008, "OTP expired"),
    AUTH_INSUFFICIENT_PERMISSIONS(2009, "Insufficient permissions"),
    
    // User Profile Errors (3000-3099)
    // Add specific user profile error codes as needed in this range
    
    // Validation Errors (4000-4099)
    VALIDATION_ERROR(4000, "Validation error"),
    VALIDATION_REQUIRED_FIELD(4001, "Required field missing"),
    VALIDATION_INVALID_FORMAT(4002, "Invalid format"),
    VALIDATION_INVALID_VALUE(4003, "Invalid value"),
    
    // Resource Errors (6000-6099)
    RESOURCE_NOT_FOUND(6000, "Resource not found"),
    RESOURCE_ALREADY_EXISTS(6001, "Resource already exists"),
    RESOURCE_ACCESS_DENIED(6002, "Access denied");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return SYSTEM_ERROR;
    }
} 