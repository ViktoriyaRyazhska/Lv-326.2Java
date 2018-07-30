package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.TicketIssueType;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tickets")
@Data
@EqualsAndHashCode(callSuper = false)
public class Ticket extends DateAudit{

    private String name;
    private String description;
    private String priority;
    private TicketIssueType ticketIssueType;

//    @OneToMany(fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL,df
//            mappedBy = "ticket")
//    private List<User> AssignedTo;

//    @ManyToOne(fetch = FetchType.LAZY,
//            cascade = CascadeType.ALL)
//    private List<User> assignedTo;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private User assignedTo;

    private Instant expirationDate;


    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private TableList tableList;


    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Board board;


//    @OneToOne(fetch = FetchType.LAZY)
//    private Status status;
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "ticket")
    private List<Comment> comments;

    @OneToOne(fetch = FetchType.LAZY)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)//
    private Backlog backlog;


    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Sprint sprint;

    @Override
    public String toString() {
        return "Ticket{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
//                ", ticketIssueType=" + ticketIssueType +
//                ", AssignedTo=" + AssignedTo +
                ", expirationDate=" + expirationDate +
//                ", tableList=" + tableList +
//                ", board=" + board +
//                ", comments=" + comments +
//                ", status=" + status +
//                ", backlog=" + backlog +
//                ", sprint=" + sprint +
                '}';
    }
}