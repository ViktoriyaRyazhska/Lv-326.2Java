package com.softserve.edu.cajillo.exception;

public class PasswordMismatchException extends BadRequestException {

    public PasswordMismatchException() {
    }

    public PasswordMismatchException(String message) {
        super(message);
    }
}
