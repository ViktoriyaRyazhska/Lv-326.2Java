package com.softserve.edu.cajillo.exception;

public class TableListNotFoundException extends DatabaseItemNotFoundException {

    public TableListNotFoundException() {
    }

    public TableListNotFoundException(String message) {
        super(message);
    }
}