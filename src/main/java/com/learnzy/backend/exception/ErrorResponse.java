package com.learnzy.backend.exception;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ErrorResponse {
    private String error;
    private String message;
    private OffsetDateTime timestamp;
}
