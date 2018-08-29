package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.SimpleBoardConverter;
import com.softserve.edu.cajillo.dto.SimpleBoardDto;
import com.softserve.edu.cajillo.entity.Board;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleBoardConverterImpl implements SimpleBoardConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Board convertToEntity(SimpleBoardDto dto) {
        return modelMapper.map(dto, Board.class);
    }

    @Override
    public SimpleBoardDto convertToDto(Board entity) {
        return modelMapper.map(entity, SimpleBoardDto.class);
    }
}
