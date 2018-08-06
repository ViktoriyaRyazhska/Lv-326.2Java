package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.BoardConverter;
import com.softserve.edu.cajillo.converter.RoleManagerConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.RoleManagerDto;
import com.softserve.edu.cajillo.entity.RoleManager;
import com.softserve.edu.cajillo.service.BoardService;
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
    private TeamConverter teamConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardConverter boardConverter;

    @Override
    public RoleManager convertToEntity(RoleManagerDto dto) {
        RoleManager entity = modelMapper.map(dto, RoleManager.class);
        if (dto.getBoardId() != null){
            entity.setBoard(boardConverter.convertToEntity(boardService.getBoard(dto.getBoardId())));
        }
        if (dto.getTeamId() != null) {
            entity.setTeam(teamConverter.convertToEntity(teamService.getTeam(dto.getTeamId())));
        }
        entity.setUser(userService.getUser(dto.getUserId()));
        return entity;
    }

    @Override
    public RoleManagerDto convertToDto(RoleManager entity) {
        RoleManagerDto dto = modelMapper.map(entity, RoleManagerDto.class);
        dto.setTeamId(entity.getTeam().getId());
        dto.setUserId(entity.getUser().getId());
        return dto;
    }
}