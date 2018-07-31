package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.service.RoleManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class RoleManagerController {

    @Autowired
    private RoleManagerService roleManagerService;
}