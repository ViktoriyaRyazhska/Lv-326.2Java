package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.converter.ticketConverter.TicketConverter;
import com.softserve.edu.cajillo.converter.ticketConverter.TicketToBoardResponseDtoConverter;
import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketConverter ticketConverter;

//    @GetMapping("/{id}")
//    public GetSingleTicketResponseDto getTicket(@PathVariable("id") Long id) {
//        Ticket ticket = ticketService.getTicket(id);
//        return ticketConverter.convertToDto(ticket);
//    }

    @GetMapping("/{id}")
    public GetSingleTicketResponseDto getTicket(@PathVariable("id") Long id) {
        return ticketService.getTicket(id);
    }

    @PostMapping
    public ResponseEntity<?> createTicket(/*@Valid*/ @RequestBody CreateTicketRequestDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal) {
        Ticket ticket = ticketService.createTicket(createTicketRequest, userPrincipal);
        return ResponseEntity.ok()
                .body(new CreateTicketResponseDto(ticket.getId(), ticket.getTableList().getId(),
                        ticket.getBoard().getId(), ticket.getName()));
    }
}