package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.BoardConverter;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.entity.Board;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardConverterImpl implements BoardConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Board convertToEntity(BoardDto dto) {
        Board board = modelMapper.map(dto, Board.class);
        return board;
    }

    @Override
    public BoardDto convertToDto(Board entity) {
        BoardDto boardDto = modelMapper.map(entity, BoardDto.class);
        return boardDto;
    }
}
