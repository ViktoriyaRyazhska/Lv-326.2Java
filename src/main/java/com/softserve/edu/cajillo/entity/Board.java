package com.softserve.edu.cajillo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;
    private String name;
    private long statusId;
    private long userId;
    private long boardTypeId;
}