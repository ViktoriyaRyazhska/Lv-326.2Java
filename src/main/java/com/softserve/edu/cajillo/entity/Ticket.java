package com.softserve.edu.cajillo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tickets")
@EqualsAndHashCode(callSuper = false)
public class Ticket extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String priority;
    private Instant createTime;
    private Instant updateTime;
    private Instant expirationDate;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private TableList tableList;

    @OneToOne(fetch = FetchType.LAZY)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "ticket")
    private List<Comment> comments = new ArrayList<>();


}