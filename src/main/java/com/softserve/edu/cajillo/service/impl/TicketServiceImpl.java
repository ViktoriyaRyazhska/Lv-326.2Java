package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.ticketConverter.TicketConverter;
import com.softserve.edu.cajillo.converter.ticketConverter.TicketToBoardResponseDtoConverter;
import com.softserve.edu.cajillo.dto.CreateTicketRequestDto;
import com.softserve.edu.cajillo.dto.GetSingleTicketResponseDto;
import com.softserve.edu.cajillo.dto.TicketForBoardResponseDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.exception.TicketNotFoundException;
import com.softserve.edu.cajillo.repository.*;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.TicketService;
import com.softserve.edu.cajillo.service.UserService;
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
    private CommentRepository commentRepository;

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TicketConverter ticketConverter;

    @Autowired
    private UserService userService;

    @Override
    public GetSingleTicketResponseDto getTicket(Long id) {
        return ticketConverter.convertToDto(ticketRepository.findById(id).orElseThrow(() -> new TicketNotFoundException(TICKET_ID_NOT_FOUND_MESSAGE + id)));
    }

    // Method for Vova, do not ask why
    public List<TicketForBoardResponseDto> getTicketsByListId(Long tableListId) {
        return ticketToBoardResponseDtoConverter.convertToDto(ticketRepository.findAllByTableListId(tableListId));
    }

    @Override
    public Ticket createTicket(CreateTicketRequestDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal) {

//        ticketConverter.convertToDto(ticket)
        Ticket ticket = new Ticket();
        ticket.setBoard(boardRepository.findById(createTicketRequest.getBoardId()).orElseThrow(RuntimeException::new));
        ticket.setName(createTicketRequest.getName());
        ticket.setTableList(tableListRepository.findById(createTicketRequest.getTableListId()).orElseThrow(RuntimeException::new));
        ticket.setCreatedBy(userService.getUser(userPrincipal.getId()));
        Ticket result = ticketRepository.save(ticket);
        return result;
    }
}