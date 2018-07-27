package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.*;
import com.softserve.edu.cajillo.entity.enums.TicketIssueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSingleTicketResponse extends BaseDto {

    private Long id;

    private String name;

    private String description;

    private String priority;

    private TicketIssueType ticketIssueType;

    private Long AssignedTo;

    private Instant expirationDate;

    private TableList tableList;

    private Board board;

    private Status status;

    private List<Comment> comments = new ArrayList<>();

    private Backlog backlog;

    private Sprint sprint;

}