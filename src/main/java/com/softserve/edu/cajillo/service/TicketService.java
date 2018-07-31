package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.CreateTicketRequestDto;
import com.softserve.edu.cajillo.entity.Ticket;

public interface TicketService {

    Ticket createTicket(CreateTicketRequestDto createTicketRequest);

    Ticket getTicket(Long id);
}