package com.learnzy.backend.exception;

public class UserNotEnrolledException extends RuntimeException {
    public UserNotEnrolledException(String message){
        super(message);
    }
}
