package com.softserve.edu.cajillo.converter;

import com.softserve.edu.cajillo.dto.CommentDto;
import com.softserve.edu.cajillo.entity.Comment;

public interface CommentConverter extends GenericConverter<CommentDto, Comment> {

    Comment convertToEntity(CommentDto dto);

    CommentDto convertToDto(Comment entity);
}
