package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.ticketConverter.TicketConverter;
import com.softserve.edu.cajillo.dto.GetSingleTicketResponseDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketConverterImpl implements TicketConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Ticket convertToEntity(GetSingleTicketResponseDto dto) {
        return modelMapper.map(dto, Ticket.class);
    }

    @Override
    public GetSingleTicketResponseDto convertToDto(Ticket entity) {
        GetSingleTicketResponseDto getSingleTicketResponseDto = modelMapper.map(entity, GetSingleTicketResponseDto.class);
        getSingleTicketResponseDto.setAssignedTo(entity.getAssignedTo().getId());
        getSingleTicketResponseDto.setCreatedBy(entity.getCreatedBy().getId());
        getSingleTicketResponseDto.setBoardId(entity.getBoard().getId());
        getSingleTicketResponseDto.setSprintId(entity.getSprint().getId());
        getSingleTicketResponseDto.setTableListId(entity.getTableList().getId());
        getSingleTicketResponseDto.setComments(commentRepository.findAllByTicketId(entity.getId()));
        return getSingleTicketResponseDto;
    }
}