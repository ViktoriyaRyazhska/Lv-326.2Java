package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PutMapping
    public CommentDto updateComment(@RequestBody CommentDto commentDto, @CurrentUser UserPrincipal userPrincipal) {
        commentDto.setUserId(userPrincipal.getId());
        return commentService.updateComment(commentDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTableList(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestBody CommentDto commentDto, @CurrentUser UserPrincipal userPrincipal) {
        commentDto.setUserId(userPrincipal.getId());
        return commentService.createComment(commentDto);
    }
}