package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.enums.TicketIssueType;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class TicketForBoardResponseDto extends BaseDto implements Serializable {

    private Long id;

    private Instant createTime;

    private Instant updateTime;

    private String name;

    private String priority;

    private TicketIssueType ticketIssueType;

    private String assignedTo;

    private Instant expirationDate;

    private Long parentTicketId;

    private Long boardId;

    private Long tableListId;

    private Integer sequenceNumber;
}
