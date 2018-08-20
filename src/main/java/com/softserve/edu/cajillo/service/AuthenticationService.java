package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.JwtAuthenticationResponseDto;
import com.softserve.edu.cajillo.dto.LoginRequestDto;
import com.softserve.edu.cajillo.dto.RegisterRequestDto;
import com.softserve.edu.cajillo.dto.ResetPasswordDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

public interface AuthenticationService {

    void registerUser(RegisterRequestDto registerRequestDto);

    void resetUserPasswordConfirm(ResetPasswordDto resetPasswordDto);

    void resetUserPasswordRequest(String email);

    JwtAuthenticationResponseDto authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest);

    JwtAuthenticationResponseDto authenticateUserGoogle(@RequestParam(value = "access_token") String accessToken);

    JwtAuthenticationResponseDto authenticateUserGithub(@RequestParam(value = "access_token") String accessToken);
}