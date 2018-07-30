package com.softserve.edu.cajillo.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnsatisfiedException extends RuntimeException{
    public UnsatisfiedException(String message) {
        super(message);
    }
}
