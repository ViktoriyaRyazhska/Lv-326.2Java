package com.softserve.edu.cajillo.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "statuses")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToOne(mappedBy = "status",
//            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
//            , optional = false
    )
//    параметр optional говорит JPA, является ли значение в этом поле обязательным или нет
//    параметр cascade говорит JPA что делать с владеемыми объектами при операциях над владельцем
    private Board board;

    @OneToOne(mappedBy = "status",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private TableList tableList;

    @OneToOne(mappedBy = "status",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Comment comment;

    @OneToOne(mappedBy = "status",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Ticket ticket;

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", board=" + board +
                ", tableList=" + tableList +
                ", comment=" + comment +
                ", ticket=" + ticket +
                '}';
    }
}