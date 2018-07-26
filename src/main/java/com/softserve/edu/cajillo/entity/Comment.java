package com.softserve.edu.cajillo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "comments")
@EqualsAndHashCode(callSuper = false)
public class Comment extends DateAudit{

    private String message;
    private Instant createTime;
    private Instant updateTime;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Ticket ticket;
}