package com.softserve.edu.cajillo.converter;

import com.softserve.edu.cajillo.dto.CommentResponseDto;
import com.softserve.edu.cajillo.entity.Comment;

public interface CommentConverter extends GenericConverter<CommentResponseDto, Comment> {

    Comment convertToEntity(CommentResponseDto dto);

    CommentResponseDto convertToDto(Comment entity);
}
