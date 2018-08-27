package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.CommentConverter;
import com.softserve.edu.cajillo.converter.ticketConverter.TicketConverter;
import com.softserve.edu.cajillo.dto.TicketDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketConverterImpl implements TicketConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentConverter commentConverter;

    @Override
    public Ticket convertToEntity(TicketDto ticketDto) {
        return modelMapper.map(ticketDto, Ticket.class);
    }


    @Override
    public TicketDto convertToDto(Ticket entity) {
        TicketDto ticketDto = modelMapper.map(entity, TicketDto.class);
        if (ticketDto.getAssignedToId() != null)
            ticketDto.setAssignedToName(entity.getAssignedTo().getFirstName() + " " + entity.getAssignedTo().getLastName());
        ticketDto.setCreatedByName(entity.getCreatedBy().getFirstName() + " " + entity.getCreatedBy().getLastName());
        ticketDto.setComments(commentConverter.convertToDto(commentRepository.findAllByTicketId(entity.getId())));
        return ticketDto;
    }
}