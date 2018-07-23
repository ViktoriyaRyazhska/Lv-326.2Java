package com.softserve.edu.cajillo.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
//    private String createTime;
//    private String updateTime;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Ticket ticket;
}