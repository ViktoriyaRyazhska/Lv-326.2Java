package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.entity.Comment;
import com.softserve.edu.cajillo.repository.CommentRepository;
import com.softserve.edu.cajillo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsByTicketId(Long ticketId) {
        return commentRepository.findAllByTicketId(ticketId);
    }

}
