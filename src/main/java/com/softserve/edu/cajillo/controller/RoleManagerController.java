package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.entity.RoleManager;
import com.softserve.edu.cajillo.service.RoleManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class RoleManagerController {

    @Autowired
    private RoleManagerService roleManagerService;

//    //- /   GET - get all teams by userId for current user
//    @GetMapping("/")
//    public List<RoleManager> getAllRoleManagersByUserId(){}

//    -/addUser  POST - add user to team
//    -/deleteUser DELETE - delete user from team
}
