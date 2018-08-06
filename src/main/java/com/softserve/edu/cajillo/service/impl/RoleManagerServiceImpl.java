package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.entity.RoleManager;
import com.softserve.edu.cajillo.exception.RoleManagerServiceException;
import com.softserve.edu.cajillo.repository.RoleManagerRepository;
import com.softserve.edu.cajillo.service.RoleManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleManagerServiceImpl implements RoleManagerService {

    private static final String ROLEMANAGERSERVICE_ID_NOT_FOUND_MESSAGE = "Could not find roleManagerService with id=";
    private static final String ROLEMANAGERSERVICE_USER_ID_NOT_FOUND_MESSAGE = "Could not find roleManagerService with user_id=";

    @Autowired
    private RoleManagerRepository roleManagerRepository;

    @Override
    public RoleManager getRoleManagerService(Long id) {
        return roleManagerRepository.findById(id).orElseThrow(
                () -> new RoleManagerServiceException(ROLEMANAGERSERVICE_ID_NOT_FOUND_MESSAGE + id));
    }

    @Override
    public List<RoleManager> getAllRoleManagerServicesByUserId(Long id) {
        return roleManagerRepository.findAllByUserId(id).orElseThrow(
                () -> new RoleManagerServiceException(ROLEMANAGERSERVICE_USER_ID_NOT_FOUND_MESSAGE + id));
    }
}