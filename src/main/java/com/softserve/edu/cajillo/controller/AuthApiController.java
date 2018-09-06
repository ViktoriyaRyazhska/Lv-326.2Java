package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.JwtAuthenticationResponseDto;
import com.softserve.edu.cajillo.dto.LoginRequestDto;
import com.softserve.edu.cajillo.dto.RegisterRequestDto;
import com.softserve.edu.cajillo.dto.ResetPasswordDto;
import com.softserve.edu.cajillo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://cajillo.ga"})
public class AuthApiController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto authorization) {
        return ResponseEntity.ok(authenticationService.authenticateUser(authorization));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public void registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        authenticationService.registerUser(registerRequestDto);
    }

    @GetMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public void resetPasswordRequest(@RequestParam("email") String userEmail) {
        authenticationService.resetUserPasswordRequest(userEmail);
    }

    @PostMapping("/reset")
    @ResponseStatus(HttpStatus.OK)
    public void resetPasswordConfirm(@RequestBody ResetPasswordDto resetPasswordDto) {
        authenticationService.resetUserPasswordConfirm(resetPasswordDto);
    }

    @GetMapping("/oauth/google")
    public JwtAuthenticationResponseDto authenticateUserWithGoogle(@RequestParam(value = "access_token") String access_token) {
        return authenticationService.authenticateUserGoogle(access_token);
    }

    @GetMapping("/oauth/github")
    public JwtAuthenticationResponseDto authenticateUserWithGithub(@RequestParam(value = "access_token") String access_token) {
        return authenticationService.authenticateUserGithub(access_token);
    }
}