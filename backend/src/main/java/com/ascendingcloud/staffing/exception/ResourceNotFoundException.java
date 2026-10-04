package com.ascendingcloud.staffing.exception;

/** Thrown when a requested record does not exist (HTTP 404). */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
