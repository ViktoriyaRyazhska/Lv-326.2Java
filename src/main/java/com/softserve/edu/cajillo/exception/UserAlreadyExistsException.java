package com.softserve.edu.cajillo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends ServiceException {

    public UserAlreadyExistsException() {
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
