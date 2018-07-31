package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.entity.enums.TicketIssueType;

import com.softserve.edu.cajillo.entity.enums.TicketTypeByBoard;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "tickets")
@Data
@EqualsAndHashCode(callSuper = false)
public class Ticket extends DateAudit {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "priority")
    private String priority;

    @Column(name = "ticket_issue_type")
    private TicketIssueType ticketIssueType;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "assigned_user_id", referencedColumnName = "id")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "created_user_id", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private User createdBy;

    @Column(name = "expiration_date")
    private Instant expirationDate;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "table_list_id", referencedColumnName = "id", nullable = false)
    private TableList tableList;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
    private Board board;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type_by_board", nullable = false)
    private TicketTypeByBoard ticketTypeByBoard;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "sprint_id", referencedColumnName = "id")
    private Sprint sprint;
}