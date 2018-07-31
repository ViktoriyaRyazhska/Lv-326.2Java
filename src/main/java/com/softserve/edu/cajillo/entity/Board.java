package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.BoardType;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "boards")
public class Board extends DateAudit {

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false)
    private BoardType boardType;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ItemsStatus status;
}