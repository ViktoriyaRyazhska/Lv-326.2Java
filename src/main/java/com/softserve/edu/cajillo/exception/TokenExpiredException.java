package com.softserve.edu.cajillo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends ServiceException {

    public TokenExpiredException() {
    }

    public TokenExpiredException(String message) {
        super(message);
    }

}
