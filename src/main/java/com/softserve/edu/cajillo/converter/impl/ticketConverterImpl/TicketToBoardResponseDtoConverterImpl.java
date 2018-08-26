package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.ticketConverter.TicketToBoardResponseDtoConverter;
import com.softserve.edu.cajillo.dto.TicketForBoardResponseDto;
import com.softserve.edu.cajillo.entity.Ticket;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketToBoardResponseDtoConverterImpl implements TicketToBoardResponseDtoConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Ticket convertToEntity(TicketForBoardResponseDto dto) {
        return modelMapper.map(dto, Ticket.class);
    }

    @Override
    public TicketForBoardResponseDto convertToDto(Ticket entity) {
        TicketForBoardResponseDto result = modelMapper.map(entity, TicketForBoardResponseDto.class);
        if (entity.getAssignedTo() != null)
            result.setAssignedTo(entity.getAssignedTo().getUsername());
        result.setTicketIssueType(entity.getTicketIssueType());
        return result;
    }
}