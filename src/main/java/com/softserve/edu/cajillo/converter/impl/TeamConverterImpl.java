package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.entity.Team;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamConverterImpl implements TeamConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Team convertToEntity(TeamDto dto) {
        return modelMapper.map(dto, Team.class);
    }

    @Override
    public TeamDto convertToDto(Team entity) {
        return modelMapper.map(entity, TeamDto.class);
    }
}