package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.ticketConverter.*;
import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.exception.TicketNotFoundException;
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
    private TicketToCreateTicketRequestDtoConverter ticketToCreateTicketRequestDtoConverter;

    @Autowired
    private TicketConverter ticketConverter;

    @Override
    public GetSingleTicketResponseDto getTicket(Long id) {
        return ticketConverter.convertToDto(ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(TICKET_ID_NOT_FOUND_MESSAGE + id)));
    }

    // Method for Vova, do not ask why
    public List<TicketForBoardResponseDto> getTicketsByListId(Long tableListId) {
        return ticketToBoardResponseDtoConverter.convertToDto(ticketRepository.findAllByTableListId(tableListId));
    }

    @Override
    public CreateTicketResponseDto createTicket(CreateTicketRequestDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal) {
        createTicketRequest.setCreatedById(userPrincipal.getId());
        Ticket ticket = ticketRepository.save(ticketToCreateTicketRequestDtoConverter.convertToEntity(createTicketRequest));
        return new CreateTicketResponseDto(ticket.getId(), ticket.getTableList().getId(), ticket.getBoard().getId());
    }
}