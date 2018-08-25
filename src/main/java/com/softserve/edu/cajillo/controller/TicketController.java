package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/{id}")
    public TicketDto getTicket(@PathVariable("id") Long id) {
        return ticketService.getTicket(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTicket(@PathVariable("id") Long id) {
        ticketService.deleteTicket(id);
    }

    @PutMapping
    public TicketDto updateTicket(@RequestBody TicketDto ticketDto) {
        return ticketService.updateTicket(ticketDto);
    }

    @PutMapping("/order")
    public void updateTicketSequenceNumber(@RequestBody OrderTicketDto orderTicketDto) {
        ticketService.updateTicketSequenceNumber(orderTicketDto);
    }

    @PostMapping
    public CreateTicketDto createTicket(@RequestBody CreateTicketDto createTicketRequest,
                                        @CurrentUser UserPrincipal userPrincipal) {
        return ticketService.createTicket(createTicketRequest, userPrincipal);
    }
}