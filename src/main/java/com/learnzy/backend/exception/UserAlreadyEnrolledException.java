package com.learnzy.backend.exception;

public class UserAlreadyEnrolledException extends RuntimeException {
    public UserAlreadyEnrolledException(String message){
        super(message);
    }
}
