package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.RoleManagerConverter;
import com.softserve.edu.cajillo.dto.RoleManagerDto;
import com.softserve.edu.cajillo.entity.RoleManager;
import com.softserve.edu.cajillo.service.TeamService;
import com.softserve.edu.cajillo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleManagerConverterImpl implements RoleManagerConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Override
    public RoleManager convertToEntity(RoleManagerDto dto) {
        RoleManager entity = modelMapper.map(dto, RoleManager.class);
        entity.setTeam(teamService.getTeam(dto.getTeam_id()));
        entity.setUser(userService.getUser(dto.getUser_id()));
        return entity;
    }

    @Override
    public RoleManagerDto convertToDto(RoleManager entity) {
        RoleManagerDto dto = modelMapper.map(entity, RoleManagerDto.class);
        dto.setTeam_id(entity.getTeam().getId());
        dto.setUser_id(entity.getUser().getId());
        return dto;
    }
}