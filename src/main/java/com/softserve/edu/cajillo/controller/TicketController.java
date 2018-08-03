package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/{id}")
    public GetSingleTicketResponseDto getTicket(@PathVariable("id") Long id) {
        return ticketService.getTicket(id);
    }

    @PostMapping
    public CreateTicketResponseDto createTicket(/*@Valid*/ @RequestBody CreateTicketRequestDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal) {
        return ticketService.createTicket(createTicketRequest, userPrincipal);
    }
}