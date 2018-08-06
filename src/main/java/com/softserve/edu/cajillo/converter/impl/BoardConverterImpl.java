package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.BoardConverter;
import com.softserve.edu.cajillo.converter.SprintConverter;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Sprint;
import com.softserve.edu.cajillo.entity.enums.BoardType;
import com.softserve.edu.cajillo.service.SprintService;
import com.softserve.edu.cajillo.service.TicketService;
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

    @Autowired
    private TicketService ticketService;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private SprintConverter sprintConverter;

    @Override
    public Board convertToEntity(BoardDto dto) {
        return modelMapper.map(dto, Board.class);
    }

    @Override
    public BoardDto convertToDto(Board entity) {
        List<TableListDto> allTableLists = tableListService.getAllTableLists(entity.getId());
        for (TableListDto listDto : allTableLists) {
            listDto.setTicketForBoardResponseDtos(ticketService.getTicketsByListId(listDto.getId()));
        }
        BoardDto dto = modelMapper.map(entity, BoardDto.class);
        if(entity.getBoardType() == BoardType.SCRUM) {
            List<SprintDto> allSprintsByBoardId = sprintService.getAllSprintsByBoardIdNotInArchive(dto.getId());
            dto.setSprintDtos(allSprintsByBoardId);
        }
        dto.setTableListDtoList(allTableLists);
        return dto;
    }
}