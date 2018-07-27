package com.softserve.edu.cajillo.exception;

public class TicketNotFoundException extends DatabaseItemNotFoundException {

    public TicketNotFoundException() {
    }

    public TicketNotFoundException(String message) {
        super(message);
    }

}
