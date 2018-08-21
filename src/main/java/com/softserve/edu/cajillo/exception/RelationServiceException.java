package com.softserve.edu.cajillo.exception;

public class RelationServiceException extends DatabaseItemNotFoundException {

    public RelationServiceException() {
    }

    public RelationServiceException(String message) {
        super(message);
    }
}