package com.softserve.edu.cajillo.exception;

public class CommentNotFoundException extends DatabaseItemNotFoundException {

    public CommentNotFoundException() {
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}