package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.CommentConverter;
import com.softserve.edu.cajillo.dto.CommentDto;
import com.softserve.edu.cajillo.entity.Comment;
import com.softserve.edu.cajillo.entity.enums.CommentStatus;
import com.softserve.edu.cajillo.exception.ResourceNotFoundException;
import com.softserve.edu.cajillo.repository.CommentRepository;
import com.softserve.edu.cajillo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentConverter commentConverter;

    public CommentDto updateComment(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentDto.getId()));
        if (commentDto.getMessage() != null)
            comment.setMessage(commentDto.getMessage());
        comment.setCommentStatus(CommentStatus.UPDATED);
        return commentConverter.convertToDto(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));
        comment.setCommentStatus(CommentStatus.DELETED);
        commentRepository.save(comment);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        commentDto.setCommentStatus(CommentStatus.CREATED);
        return commentConverter.convertToDto(commentRepository.save(commentConverter.convertToEntity(commentDto)));
    }
}
