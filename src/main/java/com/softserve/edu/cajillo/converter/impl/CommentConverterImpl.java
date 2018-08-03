package com.softserve.edu.cajillo.converter.impl;

import com.softserve.edu.cajillo.converter.CommentConverter;
import com.softserve.edu.cajillo.dto.CommentResponseDto;
import com.softserve.edu.cajillo.entity.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentConverterImpl implements CommentConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Comment convertToEntity(CommentResponseDto dto) {
        return modelMapper.map(dto, Comment.class);
    }

    @Override
    public CommentResponseDto convertToDto(Comment entity) {
        CommentResponseDto commentResponseDto = modelMapper.map(entity, CommentResponseDto.class);
        commentResponseDto.setTicketId(entity.getTicket().getId());
        commentResponseDto.setUserId(entity.getUser().getId());
        return commentResponseDto;
    }

}
