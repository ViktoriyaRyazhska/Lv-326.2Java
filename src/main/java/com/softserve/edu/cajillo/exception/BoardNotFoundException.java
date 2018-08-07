package com.softserve.edu.cajillo.exception;

public class BoardNotFoundException extends DatabaseItemNotFoundException {

    public BoardNotFoundException() {
    }

    public BoardNotFoundException(String message) {
        super(message);
    }
}