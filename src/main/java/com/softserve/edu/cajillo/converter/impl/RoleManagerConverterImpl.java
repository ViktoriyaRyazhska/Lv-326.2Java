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
        RoleManager map = modelMapper.map(dto, RoleManager.class);
        map.setTeam(teamService.getTeam(dto.getTeam_id()));
        map.setUser(userService.getUser(dto.getUser_id()));
        return map;
    }

    @Override
    public RoleManagerDto convertToDto(RoleManager entity) {
        RoleManagerDto map = modelMapper.map(entity, RoleManagerDto.class);
        map.setTeam_id(entity.getTeam().getId());
        map.setUser_id(entity.getUser().getId());
        return map;
    }
}