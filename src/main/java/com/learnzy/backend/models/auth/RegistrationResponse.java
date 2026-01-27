package com.learnzy.backend.models.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationResponse {
    private Long id;
    private String email;
    private String message;
}
