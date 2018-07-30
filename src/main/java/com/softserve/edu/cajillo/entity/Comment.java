package com.softserve.edu.cajillo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
@EqualsAndHashCode(callSuper = false)
public class Comment extends DateAudit{

    private String message;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Ticket ticket;
}