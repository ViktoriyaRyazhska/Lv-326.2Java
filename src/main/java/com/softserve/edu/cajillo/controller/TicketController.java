package com.softserve.edu.cajillo.controller;


import com.softserve.edu.cajillo.dto.CreateTicketRequest;
import com.softserve.edu.cajillo.dto.CreateTicketResponse;
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

//    @GetMapping("/{id}")
//    public UserDto getUser(@PathVariable("id") Long id) {
//        User user = userService.getUser(id);
//        return new UserDto(user.getUsername(), user.getLastName(), user.getLastName(), user.getEmail());
//    }

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
