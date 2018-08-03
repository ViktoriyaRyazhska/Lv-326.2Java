package com.softserve.edu.cajillo.converter.ticketConverter;

import com.softserve.edu.cajillo.converter.GenericConverter;
import com.softserve.edu.cajillo.dto.BaseDto;
import com.softserve.edu.cajillo.dto.CreateTicketResponseDto;
import com.softserve.edu.cajillo.dto.GetSingleTicketResponseDto;
import com.softserve.edu.cajillo.entity.Ticket;

import java.util.Base64;

public interface TicketToCreateTicketResponseDtoConverter extends GenericConverter<CreateTicketResponseDto, Ticket> {

}