package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.AvatarDto;
import com.softserve.edu.cajillo.dto.UpdateUserDto;
import com.softserve.edu.cajillo.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User getUser(Long id);

    void updateUser(Long userId, UpdateUserDto userDto);

    User getUserByEmail(String email);

    void uploadAvatar(Long userId, MultipartFile avatar);

    AvatarDto getUserAvatar(Long userId);
}