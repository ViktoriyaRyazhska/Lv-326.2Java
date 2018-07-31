package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.converter.TicketConverter;
import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketConverter ticketConverter;

    @GetMapping("/{id}")
    public GetSingleTicketResponseDto getTicket(@PathVariable("id") Long id) {
        Ticket ticket = ticketService.getTicket(id);
        return (ticketConverter.convertToDto(ticket));
    }
}