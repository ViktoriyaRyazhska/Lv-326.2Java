package com.softserve.edu.cajillo.exception;

public class RoleManagerServiceException extends DatabaseItemNotFoundException {

    public RoleManagerServiceException() {
    }

    public RoleManagerServiceException(String message) {
        super(message);
    }
}