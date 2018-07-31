package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.RoleManagerConverter;
import com.softserve.edu.cajillo.dto.RoleManagerDto;
import com.softserve.edu.cajillo.entity.RoleManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleManagerConverterImpl implements RoleManagerConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RoleManager convertToEntity(RoleManagerDto dto) {
        return modelMapper.map(dto, RoleManager.class);
    }

    @Override
    public RoleManagerDto convertToDto(RoleManager entity) {
        return modelMapper.map(entity, RoleManagerDto.class);
    }
}