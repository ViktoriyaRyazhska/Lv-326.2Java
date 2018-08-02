package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.JwtAuthenticationResponseDto;
import com.softserve.edu.cajillo.dto.LoginRequestDto;
import com.softserve.edu.cajillo.dto.RegisterRequestDto;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.exception.PasswordMismatchException;
import com.softserve.edu.cajillo.exception.UserAlreadyExistsException;
import com.softserve.edu.cajillo.repository.UserRepository;
import com.softserve.edu.cajillo.security.JwtTokenProvider;
import com.softserve.edu.cajillo.util.Base64DecoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private static final String USER_ALREADY_EXISTS_MESSAGE = "Username or email is already taken";
    private static final String USER_PASSWORD_MISMATCH_MESSAGE = "Password mismatch";

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestHeader("authorization") String authorization) {
        LoginRequestDto loginRequest = Base64DecoderUtils.decodeAuthorizationHeader(authorization);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponseDto(jwt));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public void registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByUsername(registerRequestDto.getUsername())
                || userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE);
        }
        if (registerRequestDto.getPassword().equals(registerRequestDto.getRepeatPassword())) {
            User user = new User();
            user.setUsername(registerRequestDto.getUsername());
            user.setEmail(registerRequestDto.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            userRepository.save(user);
        } else {
            throw new PasswordMismatchException(USER_PASSWORD_MISMATCH_MESSAGE);
        }
    }
}