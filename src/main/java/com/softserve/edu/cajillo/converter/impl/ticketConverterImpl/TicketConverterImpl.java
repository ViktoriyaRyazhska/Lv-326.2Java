package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.CommentConverter;
import com.softserve.edu.cajillo.converter.ticketConverter.TicketConverter;
import com.softserve.edu.cajillo.dto.CommentDto;
import com.softserve.edu.cajillo.dto.TicketDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.TicketPriority;
import com.softserve.edu.cajillo.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        ticket.setAssignedTo(userRepository.findById(dto.getAssignedToId()).get());
        ticket.setCreatedBy(userRepository.findById(dto.getCreatedById()).get());
        ticket.setTicketPriority(TicketPriority.valueOf(dto.getTicketPriority()));
        ticket.setTableList(tableListRepository.findById(dto.getTableListId()).get());
        ticket.setBoard(boardRepository.findById(dto.getBoardId()).get());
        ticket.setSprint(sprintRepository.findById(dto.getSprintId()).get());
        return ticket;
    }

    @Override
    public TicketDto convertToDto(Ticket entity) {
        TicketDto ticketDto
         = modelMapper.map(entity, TicketDto.class);
        if (entity.getAssignedTo() != null)
        ticketDto.setAssignedToId(entity.getAssignedTo().getId());
        ticketDto.setCreatedById(entity.getCreatedBy().getId());
        ticketDto.setBoardId(entity.getBoard().getId());
        if (entity.getSprint() != null)
        ticketDto.setSprintId(entity.getSprint().getId());
        ticketDto.setTableListId(entity.getTableList().getId());

        List<CommentDto> commentDtos = commentConverter.convertToDto(commentRepository.findAllByTicketId(entity.getId()));

        ticketDto.setComments(commentDtos);
        return ticketDto;
    }
}