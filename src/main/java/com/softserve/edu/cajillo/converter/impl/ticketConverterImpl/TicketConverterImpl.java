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
        if (dto.getAssignedToId() != null) {
            ticket.setAssignedTo(userRepository.findById(dto.getAssignedToId()).orElseThrow(() ->
                    new ResourceNotFoundException("User", "id", dto.getAssignedToId())));
        }
        ticket.setCreatedBy(userRepository.findById(dto.getCreatedById()).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", dto.getCreatedById())));
        ticket.setTableList(tableListRepository.findById(dto.getTableListId()).orElseThrow(() ->
                new ResourceNotFoundException("Table list", "id", dto.getTableListId())));
        ticket.setBoard(boardRepository.findById(dto.getBoardId()).orElseThrow(() ->
                new ResourceNotFoundException("Board", "id", dto.getBoardId())));
        if (dto.getSprintId() != null) {
            ticket.setSprint(sprintRepository.findById(dto.getSprintId()).orElseThrow(() ->
                    new ResourceNotFoundException("Sprint", "id", dto.getSprintId())));
        }
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
        if (ticketDto.getAssignedToId() != null)
            ticketDto.setAssignedToName(entity.getAssignedTo().getFirstName() + " " + entity.getAssignedTo().getLastName());
        ticketDto.setCreatedByName(entity.getCreatedBy().getFirstName() + " " + entity.getCreatedBy().getLastName());
        ticketDto.setComments(commentConverter.convertToDto(commentRepository.findAllByTicketId(entity.getId())));
        return ticketDto;
    }
}