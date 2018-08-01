package com.softserve.edu.cajillo.exception;

public class UserAlreadyExistsException extends ConflictException {

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
