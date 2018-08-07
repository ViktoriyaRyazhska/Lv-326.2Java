package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.CommentConverter;
import com.softserve.edu.cajillo.converter.ticketConverter.TicketConverter;
import com.softserve.edu.cajillo.dto.TicketDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.entity.enums.TicketPriority;
import com.softserve.edu.cajillo.exception.*;
import com.softserve.edu.cajillo.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketConverterImpl implements TicketConverter {
    private static final String USER_ID_NOT_FOUND_MESSAGE = "Could not find user with id = ";
    private static final String BOARD_ID_NOT_FOUND_MESSAGE = "Could not find board with id = ";
    private static final String TABLE_LIST_ID_NOT_FOUND_MESSAGE = "Could not find table list with id = ";
    private static final String SPRINT_ID_NOT_FOUND_MESSAGE = "Could not find sprint with id = ";

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private CommentConverter commentConverter;

    @Override
    public Ticket convertToEntity(TicketDto dto) {
        Ticket ticket = modelMapper.map(dto, Ticket.class);
        ticket.setAssignedTo(userRepository.findById(dto.getAssignedToId()).orElseThrow(() ->
                new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + dto.getAssignedToId())));
        ticket.setCreatedBy(userRepository.findById(dto.getCreatedById()).orElseThrow(() ->
                new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + dto.getCreatedById())));
        ticket.setTicketPriority(TicketPriority.valueOf(dto.getTicketPriority()));
        ticket.setTableList(tableListRepository.findById(dto.getTableListId()).orElseThrow(() ->
                new TableListNotFoundException(TABLE_LIST_ID_NOT_FOUND_MESSAGE + dto.getTableListId())));
        ticket.setBoard(boardRepository.findById(dto.getBoardId()).orElseThrow(() ->
                new BoardNotFoundException(BOARD_ID_NOT_FOUND_MESSAGE + dto.getBoardId())));
        ticket.setSprint(sprintRepository.findById(dto.getSprintId()).orElseThrow(() ->
                new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + dto.getSprintId())));
        return ticket;
    }

    @Override
    public TicketDto convertToDto(Ticket entity) {
        TicketDto ticketDto = modelMapper.map(entity, TicketDto.class);
        if (entity.getAssignedTo() != null)
            ticketDto.setAssignedToId(entity.getAssignedTo().getId());
        ticketDto.setCreatedById(entity.getCreatedBy().getId());
        ticketDto.setBoardId(entity.getBoard().getId());
        if (entity.getSprint() != null)
            ticketDto.setSprintId(entity.getSprint().getId());
        ticketDto.setTableListId(entity.getTableList().getId());
        ticketDto.setComments(commentConverter.convertToDto(commentRepository.findAllByTicketId(entity.getId())));
        return ticketDto;
    }
}