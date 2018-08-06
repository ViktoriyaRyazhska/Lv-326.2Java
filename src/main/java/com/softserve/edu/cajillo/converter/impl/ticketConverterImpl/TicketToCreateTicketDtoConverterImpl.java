package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.ticketConverter.TicketToCreateTicketDtoConverter;
import com.softserve.edu.cajillo.dto.CreateTicketDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.exception.UnsatisfiedException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import com.softserve.edu.cajillo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketToCreateTicketDtoConverterImpl implements TicketToCreateTicketDtoConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private UserService userService;

    @Override
    public Ticket convertToEntity(CreateTicketDto dto) {
        Ticket ticket = modelMapper.map(dto, Ticket.class);
        ticket.setBoard(boardRepository.findById(dto.getBoardId()).orElseThrow(() ->
                new UnsatisfiedException(String.format("Board with id %d not found", dto.getBoardId()))));
        ticket.setTableList(tableListRepository.findById(dto.getTableListId()).orElseThrow(() ->
                new UnsatisfiedException(String.format("TableList with id %d not found", dto.getTableListId()))));
        ticket.setCreatedBy(userService.getUser(dto.getCreatedById()));
        return ticket;
    }

    @Override
    public CreateTicketDto convertToDto(Ticket entity) {
        CreateTicketDto createTicketDto =  modelMapper.map(entity, CreateTicketDto.class);
        createTicketDto.setCreatedById(entity.getCreatedBy().getId());
        createTicketDto.setTableListId(entity.getTableList().getId());
        createTicketDto.setBoardId(entity.getBoard().getId());
        return createTicketDto;
    }
}