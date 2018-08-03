package com.softserve.edu.cajillo.controller;
import com.softserve.edu.cajillo.dto.RegisterRequestDto;
import com.softserve.edu.cajillo.dto.ResetPasswordDto;
import com.softserve.edu.cajillo.service.AuthenticationService;
import com.softserve.edu.cajillo.util.Base64DecoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestHeader("authorization") String authorization) {
        return ResponseEntity.ok(authenticationService.authenticateUser(
                Base64DecoderUtils.decodeAuthorizationHeader(authorization)));
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
}