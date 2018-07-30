package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.entity.RoleManager;

import java.util.List;

public interface RoleManagerService {

    RoleManager getRoleManagerService(Long id);

    List<RoleManager> getAllRoleManagerServicesByUser_id(Long id);
}
