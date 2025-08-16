package com.ecommerce.backend.exception.product;

public class ProductDisabledException extends RuntimeException {
    public ProductDisabledException(String message) {
        super(message);
    }
}
