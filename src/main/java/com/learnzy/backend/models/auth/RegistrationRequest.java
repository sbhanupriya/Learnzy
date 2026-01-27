package com.learnzy.backend.models.auth;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String email;
    private String password;
}
