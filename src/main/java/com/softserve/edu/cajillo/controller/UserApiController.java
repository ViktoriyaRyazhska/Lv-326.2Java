package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.converter.UserConverter;
import com.softserve.edu.cajillo.dto.AvatarDto;
import com.softserve.edu.cajillo.dto.UpdateUserDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverter userConverter;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        return userConverter.convertToDto(userService.getUser(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ACTIVE')")
    public UserDto getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return userConverter.convertToDto(userService.getUser(currentUser.getId()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ACTIVE')")
    public void deleteCurrentUser(@CurrentUser UserPrincipal currentUser) {
        userService.deleteUser(currentUser.getId());
    }

    @PostMapping("/restore")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DELETED')")
    public void restoreCurrentUser(@CurrentUser UserPrincipal currentUser) {
        userService.restoreUser(currentUser.getId());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ACTIVE')")
    public void updateCurrentUser(@CurrentUser UserPrincipal currentUser, @RequestBody UpdateUserDto userDto) {
        userService.updateUser(currentUser.getId(), userDto);
    }

    @PostMapping("/avatar")
    @PreAuthorize("hasRole('ACTIVE')")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadCurrentUserAvatar(@CurrentUser UserPrincipal currentUser,
                                        @RequestParam("file") MultipartFile avatar) {
        userService.uploadAvatar(currentUser.getId(), avatar);
    }

    @DeleteMapping("/avatar")
    @PreAuthorize("hasRole('ACTIVE')")
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteCurrentUserAvatar(@CurrentUser UserPrincipal currentUser) {
        userService.deleteUserAvatar(currentUser.getId());
    }

    @GetMapping("/avatar")
    @PreAuthorize("hasRole('ACTIVE')")
    public AvatarDto getCurrentUserAvatar(@CurrentUser UserPrincipal currentUser) {
        return userService.getUserAvatar(currentUser.getId());
    }

    @GetMapping("/{id}/avatar")
    public AvatarDto getUserAvatar(@PathVariable("id") Long id) {
        return userService.getUserAvatar(id);
    }

    @GetMapping("/available")
    public void checkIfUserEmailOrUsernameAvailable(@RequestParam(required = false, name = "email") String email,
                                                    @RequestParam(required = false, name = "username") String username) {
        userService.isAvailableUsernameAndEmail(username, email);
    }
}