package com.softserve.edu.cajillo.exception;

public class SprintNotFoundException extends DatabaseItemNotFoundException {

    public SprintNotFoundException() {
    }

    public SprintNotFoundException(String message) {
        super(message);
    }
}
