package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.dto.UpdateUserDto;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.exception.PasswordMismatchException;
import com.softserve.edu.cajillo.exception.UserNotFoundException;
import com.softserve.edu.cajillo.repository.UserRepository;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_ID_NOT_FOUND_MESSAGE = "Could not find user with id=";
    private static final String USER_USERNAME_NOT_FOUND_MESSAGE = "Could not find user with name=";
    private static final String USER_PASSWORD_MISMATCH_MESSAGE = "Password mismatch";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + id));
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(Long userId, UpdateUserDto userDto) {
        User currentUser = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(USER_USERNAME_NOT_FOUND_MESSAGE + userId));
        String oldPassword = userDto.getOldPassword();
        String newPassword = userDto.getNewPassword();
        String repeatPassword = userDto.getRepeatPassword();
        if (oldPassword != null) {
            if ((newPassword != null) && newPassword.equals(repeatPassword)
                    && passwordEncoder.matches(oldPassword, currentUser.getPassword())) { ///TODO Fix password verification
                currentUser.setPassword(passwordEncoder.encode(newPassword));
            } else {
                throw new PasswordMismatchException(USER_PASSWORD_MISMATCH_MESSAGE);
            }
        }
        currentUser.setFirstName(userDto.getFirstName());
        currentUser.setLastName(userDto.getLastName());
        userRepository.save(currentUser);
    }
}