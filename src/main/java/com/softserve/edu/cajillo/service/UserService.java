package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.UpdateUserDto;
import com.softserve.edu.cajillo.entity.User;

public interface UserService {

    User getUser(Long id);

    void save(User user);

    void updateUser(Long userId, UpdateUserDto userDto);
//            User user, String oldPassword, String newPassword, String repeatPassword);

    User getUserByEmail(String email);
}