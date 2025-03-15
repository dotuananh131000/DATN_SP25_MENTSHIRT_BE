package com.java.project.exceptions;

public class DataIntegrityViolationException extends RuntimeException {
    public DataIntegrityViolationException(String message) {
        super(message);
    }
}
