package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.ticketConverter.TicketToCreateTicketResponseDtoConverter;
import com.softserve.edu.cajillo.dto.BaseDto;
import com.softserve.edu.cajillo.dto.CreateTicketResponseDto;
import com.softserve.edu.cajillo.entity.Ticket;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketToCreateTicketResponseDtoConverterImpl implements TicketToCreateTicketResponseDtoConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Ticket convertToEntity(CreateTicketResponseDto dto) {
        return modelMapper.map(dto, Ticket.class);
    }

    @Override
    public CreateTicketResponseDto convertToDto(Ticket entity) {
        return modelMapper.map(entity, CreateTicketResponseDto.class);
    }
}