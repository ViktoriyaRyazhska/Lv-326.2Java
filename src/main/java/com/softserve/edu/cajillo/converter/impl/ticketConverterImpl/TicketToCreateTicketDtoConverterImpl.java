package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.ticketConverter.TicketToCreateTicketDtoConverter;
import com.softserve.edu.cajillo.dto.CreateTicketDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.exception.*;
import com.softserve.edu.cajillo.repository.*;
import javassist.NotFoundException;
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
    private UserRepository userRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Override
    public Ticket convertToEntity(CreateTicketDto dto) {
        Ticket ticket = modelMapper.map(dto, Ticket.class);
        ticket.setBoard(boardRepository.findById(dto.getBoardId()).orElseThrow(() ->
                new ResourceNotFoundException("Board", "id", dto.getBoardId())));
        ticket.setTableList(tableListRepository.findById(dto.getTableListId()).orElseThrow(() ->
                new ResourceNotFoundException("Table list", "id", dto.getTableListId())));
        ticket.setCreatedBy(userRepository.findById(dto.getCreatedById()).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", dto.getCreatedById())));
        if (dto.getSprintId() != null)
            ticket.setSprint(sprintRepository.findById(dto.getSprintId()).orElseThrow(() ->
                    new ResourceNotFoundException("Sprint", "id", dto.getSprintId())));
        return ticket;
    }

    @Override
    public CreateTicketDto convertToDto(Ticket entity) {
        CreateTicketDto createTicketDto = modelMapper.map(entity, CreateTicketDto.class);
        createTicketDto.setCreatedById(entity.getCreatedBy().getId());
        createTicketDto.setTableListId(entity.getTableList().getId());
        createTicketDto.setBoardId(entity.getBoard().getId());
        if (entity.getSprint() != null)
            createTicketDto.setSprintId(entity.getSprint().getId());
        return createTicketDto;
    }
}