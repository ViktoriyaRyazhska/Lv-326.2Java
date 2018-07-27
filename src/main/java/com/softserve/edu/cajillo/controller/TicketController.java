package com.softserve.edu.cajillo.controller;


import com.softserve.edu.cajillo.converter.TicketConverter;
import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketConverter ticketConverter;


//     Get single ticket by ID
    @GetMapping("/{id}")
    public GetSingleTicketResponse getTicket(@PathVariable("id") Long id) {
        return ticketConverter.convertToDto(ticketService.getTicket(id));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getTicket(@PathVariable("id") Long id) {
//        return ticketConverter.convertToDto(ticketService.getTicket(id));
//    }


    // Creating new ticket
    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTicket(/*@Valid*/ @RequestBody CreateTicketRequest createTicketRequest) {
        Ticket ticket = ticketService.createTicket(createTicketRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{ticketId}")
                .buildAndExpand(ticket.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new CreateTicketResponse(ticket.getId(), ticket.getTableList().getId(), ticket.getBoard().getId()));
    }
}
