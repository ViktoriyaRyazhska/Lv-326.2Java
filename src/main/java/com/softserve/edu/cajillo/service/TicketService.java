package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.CreateTicketDto;
import com.softserve.edu.cajillo.dto.TicketDto;
import com.softserve.edu.cajillo.dto.TicketForBoardResponseDto;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;

import java.util.List;

public interface TicketService {

    CreateTicketDto createTicket(CreateTicketDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal);

    TicketDto getTicket(Long id);

    List<TicketForBoardResponseDto> getTicketsByListId(Long tableListId);

    List<TicketForBoardResponseDto> getTicketsBySprintId(Long sprintId);

    void deleteTicketsByTableListId(Long listId);

    void recoverTicketsByListId(Long listId);

    void recoverTicketsBySprintId(Long sprintId);

    void archiveTicketsBySprintId(Long sprintId);

    TicketDto updateTicket(TicketDto ticketDto);

    void deleteTicket(Long ticketId);
}