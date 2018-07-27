package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.TicketConverter;
import com.softserve.edu.cajillo.converter.UserConverter;
import com.softserve.edu.cajillo.dto.GetSingleTicketResponse;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketConverterImpl implements TicketConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Ticket convertToEntity(GetSingleTicketResponse dto) {
        return modelMapper.map(dto, Ticket.class);
    }

    @Override
    public GetSingleTicketResponse convertToDto(Ticket entity) {
        return modelMapper.map(entity, GetSingleTicketResponse.class);
    }

}
