package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.BoardConverter;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.service.impl.TableListServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardConverterImpl implements BoardConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TableListServiceImpl tableListService;

    @Override
    public Board convertToEntity(BoardDto dto) {
        return modelMapper.map(dto, Board.class);
    }

    @Override
    public BoardDto convertToDto(Board entity) {
        List<TableListDto> allTableLists = tableListService.getAllTableLists(entity.getId());
        BoardDto dto = modelMapper.map(entity, BoardDto.class);
        dto.setTableListDtoList(allTableLists);
        return dto;
    }
}