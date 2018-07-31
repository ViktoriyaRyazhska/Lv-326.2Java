package com.softserve.edu.cajillo.exception;

public class DatabaseItemNotFoundException extends ServiceException {

    public DatabaseItemNotFoundException() {
    }

    public DatabaseItemNotFoundException(String message) {
        super(message);
    }
}