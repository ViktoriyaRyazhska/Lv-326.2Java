package com.softserve.edu.cajillo.exception;

public class UnsatisfiedException extends DatabaseItemNotFoundException {

    public UnsatisfiedException(String message) {
        super(message);
    }
}