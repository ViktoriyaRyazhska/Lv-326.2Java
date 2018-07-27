package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        User user = userService.getUser(id);
        return new UserDto(user.getUsername(), user.getLastName(), user.getLastName(), user.getEmail());
    }

    @PostMapping("/register")
    public void registrationSubmit(@ModelAttribute @Valid User registration) {
        userService.save(registration);
    }

}
