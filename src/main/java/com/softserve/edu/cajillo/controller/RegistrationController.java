package com.softserve.edu.cajillo.controller;


import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class RegistrationController {
    @Autowired
    UserService userService;


    @GetMapping("/registration")
    public String registrationForm(Model model) {
        model.addAttribute("registration", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registrationSubmit(@ModelAttribute @Valid User registration) {

        userService.save(registration);
        return "home";
    }
}