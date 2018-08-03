package com.softserve.edu.cajillo.converter.ticketConverter;

import com.softserve.edu.cajillo.converter.GenericConverter;
import com.softserve.edu.cajillo.dto.CreateTicketResponseDto;
import com.softserve.edu.cajillo.dto.GetSingleTicketResponseDto;
import com.softserve.edu.cajillo.entity.Ticket;
import org.springframework.http.RequestEntity;

public interface TicketConverter extends GenericConverter<GetSingleTicketResponseDto, Ticket> {

}