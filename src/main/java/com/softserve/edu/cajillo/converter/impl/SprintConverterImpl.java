package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.SprintConverter;
import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SprintConverterImpl implements SprintConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Sprint convertToEntity(SprintDto dto) {
        Sprint sprint = modelMapper.map(dto, Sprint.class);
        return sprint;
    }

    @Override
    public SprintDto convertToDto(Sprint entity) {
        SprintDto sprintDto = modelMapper.map(entity, SprintDto.class);
        return sprintDto;
    }
}
