package com.learnzy.backend.service;

import com.learnzy.backend.entity.Users;
import com.learnzy.backend.exception.UnauthorisedException;
import com.learnzy.backend.exception.UserAlreadyRegisteredException;
import com.learnzy.backend.exception.UserNotRegisteredException;
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
            if (userRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent())
                throw new UserAlreadyRegisteredException(String.format("email {0} is already registered", userRegistrationRequest.getEmail()));

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

            return userRegistrationResponse;
    }

    public LoginResponse login(LoginRequest loginRequest) {

        Optional<Users> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isEmpty())
            throw new UserNotRegisteredException(String.format("email {0} is not registered", loginRequest.getEmail()));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())){
            throw new UnauthorisedException("email or password is wrong");
        }

        return LoginResponse.builder()
                .email(loginRequest.getEmail())
                .token(jwtService.generateToken(user.get().getId()))
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
}
