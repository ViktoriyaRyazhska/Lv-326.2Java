package com.softserve.edu.cajillo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RelationServiceException extends ServiceException {

    public RelationServiceException() {
    }

    public RelationServiceException(String message) {
        super(message);
    }
}