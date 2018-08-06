package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.ticketConverter.*;
import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
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
    public TicketDto getTicket(Long id) {
        return ticketConverter.convertToDto(ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(TICKET_ID_NOT_FOUND_MESSAGE + id)));
    }

    // Method for Vova, do not ask why
    public List<TicketForBoardResponseDto> getTicketsByListId(Long tableListId) {
        return ticketToBoardResponseDtoConverter
                .convertToDto(ticketRepository.findAllByTableListIdAndStatus(tableListId, ItemsStatus.OPENED));
    }

    public TicketDto updateTicket(TicketDto ticketDto) {
        Ticket ticket = ticketConverter.convertToEntity(ticketDto);
        Ticket ticket1 = ticketRepository.save(ticket);
        return ticketConverter.convertToDto(ticket1);
    }

    @Override
    public CreateTicketResponseDto createTicket(CreateTicketRequestDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal) {
        createTicketRequest.setCreatedById(userPrincipal.getId());
        Ticket ticket = ticketRepository.save(ticketToCreateTicketRequestDtoConverter.convertToEntity(createTicketRequest));
        return new CreateTicketResponseDto(ticket.getName(), ticket.getId(), ticket.getTableList().getId(), ticket.getBoard().getId());
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