package com.softserve.edu.cajillo.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "board_types")
public class BoardType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL,
              mappedBy = "boardType")
    private List<Board> boards = new ArrayList<>();
}