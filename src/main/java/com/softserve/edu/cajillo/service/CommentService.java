package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.CommentDto;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto);
    void deleteComment(Long commentId);
    CommentDto updateComment(CommentDto commentDto);
}
