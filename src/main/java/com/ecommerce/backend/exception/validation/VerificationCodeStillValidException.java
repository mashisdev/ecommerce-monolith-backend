package com.ecommerce.backend.exception.validation;

public class VerificationCodeStillValidException extends RuntimeException {
    public VerificationCodeStillValidException(String message) {
        super(message);
    }
}
