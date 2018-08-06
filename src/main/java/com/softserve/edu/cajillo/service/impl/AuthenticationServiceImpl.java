package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.dto.JwtAuthenticationResponseDto;
import com.softserve.edu.cajillo.dto.LoginRequestDto;
import com.softserve.edu.cajillo.dto.RegisterRequestDto;
import com.softserve.edu.cajillo.dto.ResetPasswordDto;
import com.softserve.edu.cajillo.entity.PasswordResetToken;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.UserAccountStatus;
import com.softserve.edu.cajillo.exception.*;
import com.softserve.edu.cajillo.repository.PasswordResetTokenRepository;
import com.softserve.edu.cajillo.repository.UserRepository;
import com.softserve.edu.cajillo.security.JwtTokenProvider;
import com.softserve.edu.cajillo.service.AuthenticationService;
import com.softserve.edu.cajillo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.Instant;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String USER_EMAIL_NOT_FOUND_MESSAGE = "Could not find user with email='%s'";
    private static final String USER_ALREADY_EXISTS_MESSAGE = "Username or email is already taken";
    private static final String RESET_TOKEN_IS_NOT_VALID = "reset password token is invalid";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public JwtAuthenticationResponseDto authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponseDto(jwt);
    }

    public void registerUser(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByUsername(registerRequestDto.getUsername())
                || userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE);
        }
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setAccountStatus(UserAccountStatus.ACTIVE);
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(), "You successfully registered Cajillo project.",
                "Dear " + user.getUsername() + ",\n" +
                        "Thank you for joining Cajillo. \n" +
                        "RUN FOR YOUR LIFE!!!\n\n" +
                        "Best regards\nCajillo Team");
    }

    @Override
    public void resetUserPasswordConfirm(ResetPasswordDto resetPasswordDto) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(resetPasswordDto.getToken())
                .orElseThrow(() -> new TokenNotFoundException(RESET_TOKEN_IS_NOT_VALID));
        if (validatePasswordResetToken(token, resetPasswordDto.getUserId())) {
            User user = token.getUser();
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
            userRepository.save(user);
            passwordResetTokenRepository.delete(token);
        } else {
            passwordResetTokenRepository.delete(token);
            throw new TokenExpiredException(RESET_TOKEN_IS_NOT_VALID);
        }
    }

    @Override
    public void resetUserPasswordRequest(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format(USER_EMAIL_NOT_FOUND_MESSAGE, email)));
        String token = UUID.randomUUID().toString();
        passwordResetTokenRepository.save(new PasswordResetToken(token, user));
        emailService.sendEmail(email, "Password recovery", String.format("Token: %s\nUser id: %s", token,
                user.getId()));
    }

    private boolean validatePasswordResetToken(PasswordResetToken token, Long userId) {
        return token.getCreateTime().plusSeconds(3600 * 24).isAfter(Instant.now()) &&
                token.getUser().getId().equals(userId);
    }
}
