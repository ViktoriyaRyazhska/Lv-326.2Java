package com.softserve.edu.cajillo.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String priority;
//    private String createTime;
//    private String updateTime;
//    private String expirationDate;

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