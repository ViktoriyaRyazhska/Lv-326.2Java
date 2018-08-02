package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.CreateTicketRequestDto;
import com.softserve.edu.cajillo.dto.GetSingleTicketResponseDto;
import com.softserve.edu.cajillo.dto.TicketForBoardResponseDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;

import java.util.List;

public interface TicketService {

    Ticket createTicket(CreateTicketRequestDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal);

    GetSingleTicketResponseDto getTicket(Long id);

    List<TicketForBoardResponseDto> getTicketsByListId(Long tableListId);
}