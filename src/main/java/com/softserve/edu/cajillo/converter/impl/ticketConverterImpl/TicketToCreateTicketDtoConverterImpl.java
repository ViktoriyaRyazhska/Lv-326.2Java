package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.ticketConverter.TicketToCreateTicketDtoConverter;
import com.softserve.edu.cajillo.dto.CreateTicketDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.exception.BoardNotFoundException;
import com.softserve.edu.cajillo.exception.TableListNotFoundException;
import com.softserve.edu.cajillo.exception.UserNotFoundException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import com.softserve.edu.cajillo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketToCreateTicketDtoConverterImpl implements TicketToCreateTicketDtoConverter {

    private static final String USER_ID_NOT_FOUND_MESSAGE = "Could not find user with id = ";
    private static final String BOARD_ID_NOT_FOUND_MESSAGE = "Could not find board with id = ";
    private static final String TABLE_LIST_ID_NOT_FOUND_MESSAGE = "Could not find table list with id = ";


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Ticket convertToEntity(CreateTicketDto dto) {
        Ticket ticket = modelMapper.map(dto, Ticket.class);
        ticket.setBoard(boardRepository.findById(dto.getBoardId()).orElseThrow(() ->
                new BoardNotFoundException(BOARD_ID_NOT_FOUND_MESSAGE + dto.getBoardId())));
        ticket.setTableList(tableListRepository.findById(dto.getTableListId()).orElseThrow(() ->
                new TableListNotFoundException(TABLE_LIST_ID_NOT_FOUND_MESSAGE + dto.getTableListId())));
        ticket.setCreatedBy(userRepository.findById(dto.getCreatedById()).orElseThrow(() ->
                new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + dto.getCreatedById())));
        return ticket;
    }

    @Override
    public CreateTicketDto convertToDto(Ticket entity) {
        CreateTicketDto createTicketDto = modelMapper.map(entity, CreateTicketDto.class);
        createTicketDto.setCreatedById(entity.getCreatedBy().getId());
        createTicketDto.setTableListId(entity.getTableList().getId());
        createTicketDto.setBoardId(entity.getBoard().getId());
        return createTicketDto;
    }
}