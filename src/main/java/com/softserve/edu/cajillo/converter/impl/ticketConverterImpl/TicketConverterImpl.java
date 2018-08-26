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

import javax.swing.text.html.parser.Entity;

@Component
public class TicketConverterImpl implements TicketConverter {
    private static final String USER_ID_NOT_FOUND_MESSAGE = "Could not find user with id = ";
    private static final String BOARD_ID_NOT_FOUND_MESSAGE = "Could not find board with id = ";
    private static final String TABLE_LIST_ID_NOT_FOUND_MESSAGE = "Could not find table list with id = ";
    private static final String SPRINT_ID_NOT_FOUND_MESSAGE = "Could not find sprint with id = ";
    private static final String TICKET_ID_NOT_FOUND_MESSAGE = "Could not find ticket with id = ";

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

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Ticket convertToEntity(TicketDto dto) {
        Ticket ticket = modelMapper.map(dto, Ticket.class);
        if (dto.getAssignedToId() != null) {
            ticket.setAssignedTo(userRepository.findById(dto.getAssignedToId()).orElseThrow(() ->
                    new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + dto.getAssignedToId())));
        }
        ticket.setCreatedBy(userRepository.findById(dto.getCreatedById()).orElseThrow(() ->
                new UserNotFoundException(USER_ID_NOT_FOUND_MESSAGE + dto.getCreatedById())));
//        if (dto.getTicketPriority() != null) {
//            ticket.setTicketPriority(TicketPriority.valueOf(dto.getTicketPriority()));
//        }
        ticket.setTableList(tableListRepository.findById(dto.getTableListId()).orElseThrow(() ->
                new TableListNotFoundException(TABLE_LIST_ID_NOT_FOUND_MESSAGE + dto.getTableListId())));
        ticket.setBoard(boardRepository.findById(dto.getBoardId()).orElseThrow(() ->
                new BoardNotFoundException(BOARD_ID_NOT_FOUND_MESSAGE + dto.getBoardId())));
        if (dto.getSprintId() != null) {
            ticket.setSprint(sprintRepository.findById(dto.getSprintId()).orElseThrow(() ->
                    new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + dto.getSprintId())));
        }
//        if (dto.getParentTicketId() != null) {
//            ticket.setParentTicket(ticketRepository.findById(dto.getParentTicketId()).orElseThrow(() ->
//                    new TicketNotFoundException(TICKET_ID_NOT_FOUND_MESSAGE + dto.getParentTicketId())));
//        }
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
//        if (entity.getParentTicket() != null)
//            ticketDto.setParentTicketId(entity.getParentTicket().getId());
        if (ticketDto.getAssignedToId() != null)
            ticketDto.setAssignedToName(entity.getAssignedTo().getFirstName() + " " + entity.getAssignedTo().getLastName());
        ticketDto.setCreatedByName(entity.getCreatedBy().getFirstName() + " " + entity.getCreatedBy().getLastName());
        ticketDto.setComments(commentConverter.convertToDto(commentRepository.findAllByTicketId(entity.getId())));
        return ticketDto;
    }
}