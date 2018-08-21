package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.BoardConverter;
import com.softserve.edu.cajillo.converter.RelationConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.RelationDto;
import com.softserve.edu.cajillo.entity.Relation;
import com.softserve.edu.cajillo.service.BoardService;
import com.softserve.edu.cajillo.service.TeamService;
import com.softserve.edu.cajillo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelationConverterImpl implements RelationConverter {

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
    public Relation convertToEntity(RelationDto dto) {
        Relation entity = modelMapper.map(dto, Relation.class);
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
    public RelationDto convertToDto(Relation entity) {
        RelationDto dto = modelMapper.map(entity, RelationDto.class);
        dto.setTeamId(entity.getTeam().getId());
        dto.setUserId(entity.getUser().getId());
        return dto;
    }
}