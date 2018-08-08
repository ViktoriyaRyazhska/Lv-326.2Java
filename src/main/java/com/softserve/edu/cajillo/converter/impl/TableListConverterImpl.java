package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.TableListConverter;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.service.impl.TicketServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableListConverterImpl implements TableListConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TicketServiceImpl ticketService;

    @Override
    public TableList convertToEntity(TableListDto dto) {
        return modelMapper.map(dto, TableList.class);
    }

    @Override
    public TableListDto convertToDto(TableList entity) {
        TableListDto result = modelMapper.map(entity, TableListDto.class);
        result.setBoardId(entity.getBoard().getId());
        result.setTicketsForBoardResponse(ticketService.getTicketsByListId(entity.getId()));
        return result;
    }
}