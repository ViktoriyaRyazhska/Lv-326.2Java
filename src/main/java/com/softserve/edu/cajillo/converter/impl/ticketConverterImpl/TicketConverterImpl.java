package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.CommentConverter;
import com.softserve.edu.cajillo.converter.impl.CommentConverterImpl;
import com.softserve.edu.cajillo.converter.ticketConverter.TicketConverter;
import com.softserve.edu.cajillo.dto.CommentResponseDto;
import com.softserve.edu.cajillo.dto.GetSingleTicketResponseDto;
import com.softserve.edu.cajillo.entity.Comment;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.repository.CommentRepository;
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
    private CommentConverter commentConverter;

    @Override
    public Ticket convertToEntity(GetSingleTicketResponseDto dto) {
        return modelMapper.map(dto, Ticket.class);
    }

    @Override
    public GetSingleTicketResponseDto convertToDto(Ticket entity) {
        GetSingleTicketResponseDto getSingleTicketResponseDto
         = modelMapper.map(entity, GetSingleTicketResponseDto.class);
        getSingleTicketResponseDto.setAssignedToId(entity.getAssignedTo().getId());
        getSingleTicketResponseDto.setCreatedById(entity.getCreatedBy().getId());
        getSingleTicketResponseDto.setCreatedById(entity.getCreatedBy().getId());
        getSingleTicketResponseDto.setBoardId(entity.getBoard().getId());
        getSingleTicketResponseDto.setSprintId(entity.getSprint().getId());
        getSingleTicketResponseDto.setTableListId(entity.getTableList().getId());

        List<CommentResponseDto> commentResponseDtos = commentConverter.convertToDto(commentRepository.findAllByTicketId(entity.getId()));

        System.out.println(commentResponseDtos);
        getSingleTicketResponseDto.setComments(commentResponseDtos);
        return getSingleTicketResponseDto;
    }
}