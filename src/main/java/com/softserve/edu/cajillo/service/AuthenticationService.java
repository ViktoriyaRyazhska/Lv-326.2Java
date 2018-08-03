package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.JwtAuthenticationResponseDto;
import com.softserve.edu.cajillo.dto.LoginRequestDto;
import com.softserve.edu.cajillo.dto.RegisterRequestDto;
import com.softserve.edu.cajillo.dto.ResetPasswordDto;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface AuthenticationService {

    void registerUser(RegisterRequestDto registerRequestDto);

    void resetUserPasswordConfirm(ResetPasswordDto resetPasswordDto);

    void resetUserPasswordRequest(String email);

    JwtAuthenticationResponseDto authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest);

}