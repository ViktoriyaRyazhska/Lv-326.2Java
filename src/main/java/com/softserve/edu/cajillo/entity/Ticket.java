package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.TicketIssueType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tickets")
@EqualsAndHashCode(callSuper = false)
public class Ticket extends DateAudit{

    private String name;
    private String description;
    private String priority;
    private TicketIssueType ticketIssueType;
    private Long AssignedTo;

    private Instant expirationDate;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private TableList tableList;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Board board;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "ticket")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Backlog backlog;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Sprint sprint;
}