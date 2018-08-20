package com.softserve.edu.cajillo.service.impl;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.softserve.edu.cajillo.dto.JwtAuthenticationResponseDto;
import com.softserve.edu.cajillo.dto.LoginRequestDto;
import com.softserve.edu.cajillo.dto.RegisterRequestDto;
import com.softserve.edu.cajillo.dto.ResetPasswordDto;
import com.softserve.edu.cajillo.entity.PasswordResetToken;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.UserAccountStatus;
import com.softserve.edu.cajillo.exception.TokenExpiredException;
import com.softserve.edu.cajillo.exception.TokenNotFoundException;
import com.softserve.edu.cajillo.exception.UserAlreadyExistsException;
import com.softserve.edu.cajillo.exception.UserNotFoundException;
import com.softserve.edu.cajillo.repository.PasswordResetTokenRepository;
import com.softserve.edu.cajillo.repository.UserRepository;
import com.softserve.edu.cajillo.security.JwtTokenProvider;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.AuthenticationService;
import com.softserve.edu.cajillo.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String USER_EMAIL_NOT_FOUND_MESSAGE = "Could not find user with email='%s'";
    private static final String USER_ALREADY_EXISTS_MESSAGE = "Username or email is already taken";
    private static final String RESET_TOKEN_IS_NOT_VALID = "reset password token is invalid";
    private static final String GOOGLE_TOKEN_EXCHANGE = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=";
    private static final String GITHUB_TOKEN_EXCHANGE = "https://api.github.com//user?access_token=";
    private static final String EMAIL = "email";
    private static final String VERIFIED_EMAIL = "verified_email";

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
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken((UserPrincipal) authentication.getPrincipal());
        return new JwtAuthenticationResponseDto(jwt);
    }


    @Override
    public JwtAuthenticationResponseDto authenticateUserGoogle(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> payload = restTemplate.getForObject(GOOGLE_TOKEN_EXCHANGE + accessToken, Map.class);

            String email = (String) payload.get(EMAIL);
            boolean verifiedEmail = (Boolean) payload.get(VERIFIED_EMAIL);

            if (verifiedEmail) {
                Optional<User> userByEmail = userRepository.findUserByEmail(email);
                if (userByEmail.isPresent()) {
                    log.error("User credentials are already taken");
                    UserPrincipal userPrincipal = UserPrincipal.create(userByEmail.get());
                    String jwt = tokenProvider.generateToken(userPrincipal);
                    return new JwtAuthenticationResponseDto(jwt);
                } else {
                    registerUserGoogle(email);
                }
            }
        } catch (Exception e) {
            log.error("Cannot authenticate user by google accessToken", e.getMessage());
        }
        return null;
    }


    @Override
    public JwtAuthenticationResponseDto authenticateUserGithub(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> payload = restTemplate.getForObject(GITHUB_TOKEN_EXCHANGE + accessToken, Map.class);

            String username = (String) payload.get("login");
            if (accessToken != null) {
                Optional<User> userByUsername = userRepository.findUserByUsername(username);
                if (userByUsername.isPresent()) {
                    log.error("User credentials are already taken");
                    UserPrincipal userPrincipal = UserPrincipal.create(userByUsername.get());
                    String jwt = tokenProvider.generateToken(userPrincipal);
                    return new JwtAuthenticationResponseDto(jwt);
                } else {
                    registerUserGithub(username);
                }
            }
        } catch (Exception e) {
            log.error("Cannot authenticate user by github accessToken", e.getMessage());
        }
        return null;
    }


    public void registerUserGoogle(String email) {
        log.info("Registering new user with email = " + email);
        User user = new User();
        user.setUsername(email.substring(0, email.indexOf('@')));
        user.setEmail(email);
        user.setAccountStatus(UserAccountStatus.ACTIVE);
        log.info("Creating new user: " + user);
        userRepository.save(user);
        emailSend(user);
    }

    public void registerUserGithub(String username) {
        log.info("Registering new user with username = " + username);
        User user = new User();
        user.setUsername(username);
//        user.setEmail(username + "@gmail.com");
        user.setAccountStatus(UserAccountStatus.ACTIVE);
        log.info("Creating new user: " + user);
        userRepository.save(user);
        emailSend(user);
    }


    public void registerUser(RegisterRequestDto registerRequestDto) {
        log.info("Registering new user with email = " + registerRequestDto.getEmail()
                + " and username = " + registerRequestDto.getUsername());
        if (userRepository.existsByUsername(registerRequestDto.getUsername())
                || userRepository.existsByEmail(registerRequestDto.getEmail())) {
            log.error("User credentials are already taken");
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE);
        }
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setAccountStatus(UserAccountStatus.ACTIVE);
        log.info("Creating new user: " + user);
        userRepository.save(user);
        emailSend(user);
    }

    private void emailSend(User user) {
        emailService.sendEmail(user.getEmail(), "You successfully registered Cajillo project.",
                "Dear " + user.getUsername() + ",\n" +
                        "Thank you for joining Cajillo. \n" +
                        "RUN FOR YOUR LIFE!!!\n\n" +
                        "Best regards\nCajillo Team");
    }

    @Override
    public void resetUserPasswordConfirm(ResetPasswordDto resetPasswordDto) {
        log.info("Confirming password reset for user with id = " + resetPasswordDto.getUserId());
        PasswordResetToken token = passwordResetTokenRepository.findByToken(resetPasswordDto.getToken())
                .orElseThrow(() -> new TokenNotFoundException(RESET_TOKEN_IS_NOT_VALID));
        if (validatePasswordResetToken(token, resetPasswordDto.getUserId())) {
            log.info("Token is valid. Setting new password and saving user.");
            User user = token.getUser();
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
            userRepository.save(user);
            passwordResetTokenRepository.delete(token);
        } else {
            log.error("Password reset token is invalid");
            passwordResetTokenRepository.delete(token);
            throw new TokenExpiredException(RESET_TOKEN_IS_NOT_VALID);
        }
    }

    @Override
    public void resetUserPasswordRequest(String email) {
        log.info("Resetting password for user  with email " + email);
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