package com.softserve.edu.cajillo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "history_logs")
@Data
public class HistoryLog extends DateAudit{

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User user;

    private String message;
}
