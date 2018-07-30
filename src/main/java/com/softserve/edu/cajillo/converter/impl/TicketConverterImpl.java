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
        GetSingleTicketResponse dto = modelMapper.map(entity, GetSingleTicketResponse.class);
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPriority(entity.getPriority());
        dto.setAssignedTo(entity.getAssignedTo().getId());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setTableListOId(entity.getTableList().getId());
        dto.setBoardId(entity.getBoard().getId());
        dto.setStatus(entity.getStatus().getName());
        dto.setBacklogId(entity.getBacklog().getId());
        dto.setSprintId(entity.getSprint().getId());
        return dto;
    }

}
