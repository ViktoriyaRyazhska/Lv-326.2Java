package com.softserve.edu.cajillo.exception;

public class TokenNotFoundException extends DatabaseItemNotFoundException {

    public TokenNotFoundException() {
    }

    public TokenNotFoundException(String message) {
        super(message);
    }
}