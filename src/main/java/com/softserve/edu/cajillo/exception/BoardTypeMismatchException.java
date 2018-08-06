package com.softserve.edu.cajillo.exception;

public class BoardTypeMismatchException extends ServiceException {

    public BoardTypeMismatchException() {
    }

    public BoardTypeMismatchException(String message) {
        super(message);
    }
}