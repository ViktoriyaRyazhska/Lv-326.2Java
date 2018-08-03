package com.softserve.edu.cajillo.exception;

public class TokenExpiredException extends ServiceException {

    public TokenExpiredException() {
    }

    public TokenExpiredException(String message) {
        super(message);
    }

}
