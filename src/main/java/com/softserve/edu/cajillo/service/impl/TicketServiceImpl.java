package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.dto.CreateTicketRequestDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.exception.TicketNotFoundException;
import com.softserve.edu.cajillo.repository.*;
import com.softserve.edu.cajillo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    private static final String TICKET_ID_NOT_FOUND_MESSAGE = "Could not find ticket with id=";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public Ticket getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(TICKET_ID_NOT_FOUND_MESSAGE + id));
        return ticket;
    }

    @Override
    public Ticket createTicket(CreateTicketRequestDto createTicketRequest) {
        Ticket ticket = new Ticket();
        ticket.setName(createTicketRequest.getName());
        ticket.setTableList(tableListRepository.findById(createTicketRequest.getTableListId()).orElseThrow(RuntimeException::new));
        ticket.setBoard(boardRepository.findById(createTicketRequest.getBoardId()).orElseThrow(RuntimeException::new));
        Ticket result = ticketRepository.save(ticket);
        return result;
    }
}