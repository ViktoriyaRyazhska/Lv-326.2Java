package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.entity.enums.TicketIssueType;
import com.softserve.edu.cajillo.entity.enums.TicketPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto extends BaseDto {

    private Long id;

    private ItemsStatus status;

    private Instant createTime;

    private Instant updateTime;

    private String description;

    private Instant expirationDate;

    private String name;

    private TicketPriority ticketPriority;

    private Long assignedToId;

    private Long createdById;

    private TicketIssueType ticketIssueType;

    private String assignedToName;

    private String createdByName;

    private Long boardId;

    private Long sprintId;

    private Long tableListId;

    private Integer sequenceNumber;

    private Ticket.Estimation estimation;

    private List<CommentDto> comments;
}