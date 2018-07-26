package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.CreateTicketRequest;
import com.softserve.edu.cajillo.entity.Ticket;

public interface TicketService {

    Ticket createTicket(CreateTicketRequest createTicketRequest);

}
