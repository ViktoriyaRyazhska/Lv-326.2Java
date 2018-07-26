package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.entity.User;

public interface UserService {

    User getUser(Long id);

    void updateUser(User user, String oldPassword, String newPassword, String repeatPassword);
}
