package com.softserve.edu.cajillo.dto;

import lombok.Data;

@Data
public class OrderTicketDto {

    private Long ticketId;

    private Long tableListId;

    private int sequenceNumber;

    private Long sprintId;
}
