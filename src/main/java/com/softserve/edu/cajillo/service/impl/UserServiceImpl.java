package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.exception.UserNotFoundException;
import com.softserve.edu.cajillo.repository.UserRepository;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_ID_NOT_FOUND_MESSAGE = "Could not find user with id=";

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + id));
    }
}
