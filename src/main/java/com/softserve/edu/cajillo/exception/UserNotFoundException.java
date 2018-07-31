package com.softserve.edu.cajillo.exception;

public class UserNotFoundException extends DatabaseItemNotFoundException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}