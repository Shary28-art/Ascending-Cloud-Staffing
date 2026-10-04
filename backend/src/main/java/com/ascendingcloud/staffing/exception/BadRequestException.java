package com.ascendingcloud.staffing.exception;

/** Thrown when the client sends a request that cannot be processed (HTTP 400). */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
