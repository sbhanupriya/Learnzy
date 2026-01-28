package com.learnzy.backend.exception.custom;

public class DuplicateEnrollmentException extends RuntimeException {
    public DuplicateEnrollmentException(String message){
        super(message);
    }
}
