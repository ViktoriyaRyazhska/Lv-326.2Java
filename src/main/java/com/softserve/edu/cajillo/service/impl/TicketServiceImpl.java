package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.ticketConverter.*;
import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.exception.TicketNotFoundException;
import com.softserve.edu.cajillo.exception.UnsatisfiedException;
import com.softserve.edu.cajillo.repository.*;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private static final String TICKET_ID_NOT_FOUND_MESSAGE = "Could not find ticket with id=";
    private static final String TICKETS_WITH_LIST_ID_NOT_FOUND_MESSAGE = "Could not find tickets with list id=";

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketToBoardResponseDtoConverter ticketToBoardResponseDtoConverter;

    @Autowired
    private TicketToCreateTicketDtoConverter ticketToCreateTicketDtoConverter;

    @Autowired
    private TicketConverter ticketConverter;

    @Override
    public TicketDto getTicket(Long id) {
        return ticketConverter.convertToDto(ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(TICKET_ID_NOT_FOUND_MESSAGE + id)));
    }

    public List<TicketForBoardResponseDto> getTicketsByListId(Long tableListId) {
        return ticketToBoardResponseDtoConverter
                .convertToDto(ticketRepository.findAllByTableListIdAndStatus(tableListId, ItemsStatus.OPENED));
    }

    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new UnsatisfiedException(String.format("Ticket with id %d not found", ticketId)));
        ticket.setStatus(ItemsStatus.DELETED);
        ticketRepository.save(ticket);
    }

    public TicketDto updateTicket(TicketDto ticketDto) {
        return ticketConverter.convertToDto(ticketRepository.save(ticketConverter.convertToEntity(ticketDto)));
    }

    @Override
    public CreateTicketDto createTicket(CreateTicketDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal) {
        createTicketRequest.setCreatedById(userPrincipal.getId());
        Ticket ticket = ticketToCreateTicketDtoConverter.convertToEntity(createTicketRequest);
        ticket.setStatus(ItemsStatus.OPENED);
        return ticketToCreateTicketDtoConverter.convertToDto(ticketRepository.save(ticket));
    }

    public void deleteTicketsByTableListId(Long listId) {
        List<Ticket> allByTableListId = ticketRepository.findAllByTableListIdAndStatus(listId, ItemsStatus.OPENED);
        for (Ticket ticket : allByTableListId) {
            ticket.setStatus(ItemsStatus.DELETED);
            ticketRepository.save(ticket);
        }
    }

    public void recoverTicketsByListId(Long listId) {
        List<Ticket> tickets = ticketRepository.findAllByTableListIdAndStatus(listId, ItemsStatus.DELETED);
        for (Ticket ticket : tickets) {
            ticket.setStatus(ItemsStatus.OPENED);
            ticketRepository.save(ticket);
        }
    }
}