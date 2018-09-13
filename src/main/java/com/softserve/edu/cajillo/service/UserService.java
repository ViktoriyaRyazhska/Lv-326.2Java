package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.dto.AvatarDto;
import com.softserve.edu.cajillo.dto.UpdateUserDto;
import com.softserve.edu.cajillo.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User getUser(Long id);

    void updateUser(Long userId, UpdateUserDto userDto);

    void restoreUser(Long id);

    User getUserByEmail(String email);

    void uploadAvatar(Long userId, MultipartFile avatar);

    AvatarDto getUserAvatar(Long userId);

    void isAvailableUsernameAndEmail(String username, String email);

    void deleteUserAvatar(Long id);

    void deleteUser(Long id);

    void changeChosenLanguage(String language, UserPrincipal userPrincipal);
}