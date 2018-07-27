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
    private static final String USER_USERNAME_NOT_FOUND_MESSAGE = "Could not find user with name=";

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + id));
    }
    @Override
    public void save(User user){
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user, String oldPassword, String newPassword, String repeatPassword) {
        User currentUser = userRepository.findUserByUsername(user.getUsername()).orElseThrow(() ->
                new UserNotFoundException(USER_USERNAME_NOT_FOUND_MESSAGE + user.getUsername()));
        if ((oldPassword != null) && (newPassword != null) && (repeatPassword != null)) {
            if (newPassword.equals(repeatPassword) && currentUser.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
            } else {
                user.setPassword(currentUser.getPassword());
            }
        }
        user.setId(currentUser.getId());
        user.setCreateTime(currentUser.getCreateTime());
        user.setBoards(currentUser.getBoards());
        user.setAvatar(currentUser.getAvatar());
        user.setRoleManagers(currentUser.getRoleManagers());
        userRepository.save(user);
    }
}
