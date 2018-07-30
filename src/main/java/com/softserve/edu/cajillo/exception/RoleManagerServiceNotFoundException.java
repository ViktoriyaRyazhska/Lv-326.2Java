package com.softserve.edu.cajillo.exception;

public class RoleManagerServiceNotFoundException extends DatabaseItemNotFoundException {

    public RoleManagerServiceNotFoundException() {
    }

    public RoleManagerServiceNotFoundException(String message) {
        super(message);
    }

}
