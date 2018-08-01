package com.softserve.edu.cajillo.exception;

public class BadRequestException extends ServiceException {

    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

}
