package com.softserve.edu.cajillo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "table_lists")
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = "board")
public class TableList extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Board board;

    @OneToOne(fetch = FetchType.LAZY)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "tableList")
    private List<Ticket> tickets = new ArrayList<>();

}