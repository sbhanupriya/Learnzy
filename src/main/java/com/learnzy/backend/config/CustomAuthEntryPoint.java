package com.learnzy.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnzy.backend.exception.ErrorResponse;
import com.learnzy.backend.exception.custom.UnauthorisedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.OffsetDateTime;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .error("Unauthorised")
                .timestamp(OffsetDateTime.now())
                .message("JWT token is missing")
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
