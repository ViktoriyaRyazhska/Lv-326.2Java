package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.UserConverter;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public User convertToEntity(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    @Override
    public UserDto convertToDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }
}