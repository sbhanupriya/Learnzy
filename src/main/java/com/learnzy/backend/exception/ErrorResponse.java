package com.learnzy.backend.exception;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ErrorResponse {
    private String error;
    private String message;
    private OffsetDateTime timestamp;
}
