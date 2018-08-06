package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.CreateTicketRequestDto;
import com.softserve.edu.cajillo.dto.CreateTicketResponseDto;
import com.softserve.edu.cajillo.dto.TicketDto;
import com.softserve.edu.cajillo.dto.TicketForBoardResponseDto;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;

import java.util.List;

public interface TicketService {

    CreateTicketResponseDto createTicket(CreateTicketRequestDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal);

    TicketDto getTicket(Long id);

    List<TicketForBoardResponseDto> getTicketsByListId(Long tableListId);

    TicketDto updateTicket(TicketDto ticketDto);
}