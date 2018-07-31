package com.softserve.edu.cajillo.exception;

public class TeamNotFoundException extends DatabaseItemNotFoundException {

    public TeamNotFoundException() {
    }

    public TeamNotFoundException(String message) {
        super(message);
    }
}