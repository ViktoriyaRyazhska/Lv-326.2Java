package com.softserve.edu.cajillo.service.impl;


import lombok.extern.slf4j.Slf4j;
import com.softserve.edu.cajillo.exception.*;
import com.softserve.edu.cajillo.entity.User;
import org.springframework.stereotype.Service;
import com.softserve.edu.cajillo.dto.AvatarDto;
import com.softserve.edu.cajillo.dto.UpdateUserDto;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.web.multipart.MultipartFile;
import com.softserve.edu.cajillo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.softserve.edu.cajillo.entity.enums.UserAccountStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String USER_EMAIL_TAKEN = "Email is already taken";
    private static final String USER_USERNAME_TAKEN = "Username is already taken";
    private static final String USER_ID_NOT_FOUND_MESSAGE = "Could not find user with id=";
    private static final String UNSUPPORTED_MIME_TYPES_ERROR_MESSAGE = "Unsupported media type";
    private static final String REQUEST_ENTITY_TOO_LARGE_ERROR_MESSAGE = "Avatar size is too large. Maximum size is 256 KiB";
    private static final String FILES_SAVE_ERROR_MESSAGE = "Could not save file for user with id=%s";
    private static final String USER_USERNAME_NOT_FOUND_MESSAGE_BY_EMAIL = "Could not find user with email=";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + id));
    }

    @Override
    public void updateUser(Long userId, UpdateUserDto userDto) {
        User currentUser = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + userId));
        String oldPassword = userDto.getOldPassword();
        String newPassword = userDto.getNewPassword();
        if ((oldPassword != null) && (newPassword != null)
                && passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            currentUser.setPassword(passwordEncoder.encode(newPassword));
        }
        currentUser.setFirstName(userDto.getFirstName());
        currentUser.setLastName(userDto.getLastName());
        log.info("Updating user " + currentUser);
        userRepository.save(currentUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + id));
        user.setAccountStatus(UserAccountStatus.DELETED);
        log.info("Deleting user " + user);
        userRepository.save(user);
    }

    @Override
    public void restoreUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + id));
        user.setAccountStatus(UserAccountStatus.ACTIVE);
        log.info("Restoring user " + user);
        userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        log.info("Getting user by email: " + email);
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new UserNotFoundException(USER_USERNAME_NOT_FOUND_MESSAGE_BY_EMAIL + email));
    }

    public void uploadAvatar(Long userId, MultipartFile avatar) {
        log.info("Uploading avatar for user with id: " + userId);
        if (avatar.getSize() > (256 * 1024)) {
            log.error("Avatar file is to large. Size = " + avatar.getSize() + ", max size is " + (256 * 1024));
            throw new UnsupportedOperationException(REQUEST_ENTITY_TOO_LARGE_ERROR_MESSAGE);
        } else if (!Arrays.asList("image/jpeg", "image/pjpeg", "image/png").contains(avatar.getContentType())) {
            log.error(avatar.getContentType() + " is not supported");
            throw new UnsupportedOperationException(UNSUPPORTED_MIME_TYPES_ERROR_MESSAGE);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE));
        try {
            user.setAvatar(Base64.getMimeEncoder().encodeToString(avatar.getBytes()));
            userRepository.save(user);
        } catch (IOException e) {
            log.error(e.toString());
            throw new FileOperationException(String.format(FILES_SAVE_ERROR_MESSAGE, userId));
        }
    }

    @Override
    public AvatarDto getUserAvatar(Long userId) {
        log.info("Getting avatar for user with id: " + userId);
        return new AvatarDto(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE)).getAvatar());
    }

    @Override
    public void deleteUserAvatar(Long userId) {
        log.info("Deleting avatar for user with id: " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE));
        user.setAvatar(null);
        userRepository.save(user);
    }

    @Override
    public void isAvailableUsernameAndEmail(String username, String email) {
        if ((username != null) && userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException(USER_USERNAME_TAKEN);
        }
        if ((email != null) && userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(USER_EMAIL_TAKEN);
        }
    }
}