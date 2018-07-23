package com.softserve.edu.cajillo.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "table_lists")
public class TableList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
//    private String createTime;
//    private String updateTime;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Board board;

    @OneToOne(fetch = FetchType.LAZY)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "tableList")
    private List<Ticket> tickets = new ArrayList<>();

}