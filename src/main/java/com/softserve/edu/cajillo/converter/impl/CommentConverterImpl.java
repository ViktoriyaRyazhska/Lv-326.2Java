package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.CommentConverter;
import com.softserve.edu.cajillo.dto.CommentDto;
import com.softserve.edu.cajillo.entity.Comment;
import com.softserve.edu.cajillo.entity.enums.CommentStatus;
import com.softserve.edu.cajillo.exception.UnsatisfiedException;
import com.softserve.edu.cajillo.repository.TicketRepository;
import com.softserve.edu.cajillo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentConverterImpl implements CommentConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Comment convertToEntity(CommentDto dto) {
        Comment comment = modelMapper.map(dto, Comment.class);
        comment.setUser(userRepository.findById(dto.getUserId()).orElseThrow(() -> new UnsatisfiedException(String.format("User with id %d not found", dto.getUserId()))));
        comment.setTicket(ticketRepository.findById(dto.getTicketId()).orElseThrow(() -> new UnsatisfiedException(String.format("Ticket with id %d not found", dto.getTicketId()))));
        return comment;
    }

    @Override
    public CommentDto convertToDto(Comment entity) {
        CommentDto commentDto = modelMapper.map(entity, CommentDto.class);
        commentDto.setCommentId(entity.getId());
        commentDto.setTicketId(entity.getTicket().getId());
        commentDto.setUserId(entity.getUser().getId());
        return commentDto;
    }

}
