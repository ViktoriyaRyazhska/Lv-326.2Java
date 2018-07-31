package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.TicketConverter;
import com.softserve.edu.cajillo.dto.GetSingleTicketResponseDto;
import com.softserve.edu.cajillo.entity.Ticket;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketConverterImpl implements TicketConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Ticket convertToEntity(GetSingleTicketResponseDto dto) {
        return modelMapper.map(dto, Ticket.class);
    }

    @Override
    public GetSingleTicketResponseDto convertToDto(Ticket entity) {
        return modelMapper.map(entity, GetSingleTicketResponseDto.class);
    }
}