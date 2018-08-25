package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.dto.JwtAuthenticationResponseDto;
import com.softserve.edu.cajillo.dto.LoginRequestDto;
import com.softserve.edu.cajillo.dto.RegisterRequestDto;
import com.softserve.edu.cajillo.dto.ResetPasswordDto;
import com.softserve.edu.cajillo.entity.PasswordResetToken;
import com.softserve.edu.cajillo.entity.User;
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
import org.apache.commons.lang3.RandomStringUtils;
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
    private static final String EMAIL_TOPIC = "You successfully registered Cajillo project.";

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
            String email = (String) payload.get("email");
            if (accessToken != null) {
                Optional<User> userByUsername = userRepository.findUserByUsername(username);
                if (userByUsername.isPresent()) {
                    log.error("User credentials are already taken");
                    UserPrincipal userPrincipal = UserPrincipal.create(userByUsername.get());
                    String jwt = tokenProvider.generateToken(userPrincipal);
                    return new JwtAuthenticationResponseDto(jwt);
                } else {
                    registerUserGithub(username, email);
                }
            }
        } catch (Exception e) {
            log.error("Cannot authenticate user by github accessToken", e.getMessage());
        }
        return null;
    }

    public void registerUserGoogle(String email) {
        log.info("Registering new user with email = " + email);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email.substring(0, email.indexOf('@')), email, encodedPassword, User.SignupType.GOOGLE);
        log.info("Creating new user: " + user);
        userRepository.save(user);
        emailSend(user, getMessage(user.getUsername(), password));
    }

    public void registerUserGithub(String username, String email) {
        log.info("Registering new user with username = " + username);
        String password = RandomStringUtils.randomAlphanumeric(8);
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, encodedPassword, User.SignupType.GITHUB);
        log.info("Creating new user: " + user);
        userRepository.save(user);
        if (user.getEmail() != null) {
            emailSend(user, getMessage(username, password));
        }
    }

    public void registerUser(RegisterRequestDto registerRequestDto) {
        log.info("Registering new user with email = " + registerRequestDto.getEmail()
                + " and username = " + registerRequestDto.getUsername());
        if (userRepository.existsByUsername(registerRequestDto.getUsername())
                || userRepository.existsByEmail(registerRequestDto.getEmail())) {
            log.error("User credentials are already taken");
            throw new UserAlreadyExistsException(USER_ALREADY_EXISTS_MESSAGE);
        }
        User user = new User(registerRequestDto.getUsername(),registerRequestDto.getEmail(),
                passwordEncoder.encode(registerRequestDto.getPassword()), User.SignupType.GENERAL);
        log.info("Creating new user: " + user);
        userRepository.save(user);
        emailSend(user, getMessage(user.getUsername()));
    }

    private void emailSend(User user, String message) {
        if (user.getSignupType() == User.SignupType.GENERAL) {
            emailService.sendEmail(user.getEmail(), EMAIL_TOPIC, message);
        } else {
            emailService.sendEmail(user.getEmail(), EMAIL_TOPIC, message);
        }
    }

    private String getMessage(String username) {
        return "Dear " + username + ",\n" +
                "Thank you for joining Cajillo. \n" +
                "RUN FOR YOUR LIFE!!!\n\n" +
                "Best regards\nCajillo Team";
    }

    private String getMessage(String username, String password) {
        return "Dear " + username + ",\n" +
                "Thank you for joining Cajillo.\n" +
                "For general login use your \n  username: " +
                username + "\n  password: " +
                password + "\n" +
                "RUN FOR YOUR LIFE!!!\n\n" +
                "Best regards\nCajillo Team";
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