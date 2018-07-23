package com.softserve.edu.cajillo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "board_types")
public class BoardType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardTypeId;
    private String name;
}