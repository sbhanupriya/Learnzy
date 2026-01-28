package com.learnzy.backend.service;

import com.learnzy.backend.entity.Users;
import com.learnzy.backend.exception.custom.UnauthorisedException;
import com.learnzy.backend.exception.custom.DuplicateEmailException;
import com.learnzy.backend.models.auth.LoginRequest;
import com.learnzy.backend.models.auth.LoginResponse;
import com.learnzy.backend.models.auth.RegistrationRequest;
import com.learnzy.backend.models.auth.RegistrationResponse;
import com.learnzy.backend.repository.UserRepository;
import com.learnzy.backend.utils.JwtService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public RegistrationResponse register(RegistrationRequest userRegistrationRequest) {


            if (userRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent()){
                log.error(String.format("email %s is already registered", userRegistrationRequest.getEmail()));
                throw new DuplicateEmailException(String.format("email $s is already registered", userRegistrationRequest.getEmail()));
            }

            Users user = Users.builder()
                    .email(userRegistrationRequest.getEmail().toLowerCase())
                    .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                    .build();

            userRepository.save(user);

            RegistrationResponse userRegistrationResponse = RegistrationResponse.builder()
                    .email(userRegistrationRequest.getEmail())
                    .id(user.getId())
                    .message("User registered successfully")
                    .build();

            log.info(String.format("Registered user with email %s", userRegistrationRequest.getEmail()));
            return userRegistrationResponse;
    }

    public LoginResponse login(LoginRequest loginRequest) {

        Optional<Users> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isEmpty() || (!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword()))){
            log.error(String.format("Login Failed as invalid credentials for %s", loginRequest.getEmail()));
            throw new UnauthorisedException("Invalid email or password");
        }

        log.info("User {} logged in", loginRequest.getEmail());

        return LoginResponse.builder()
                .email(loginRequest.getEmail())
                .token(jwtService.generateToken(user.get().getId()))
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
}
