package com.softserve.edu.cajillo.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "board")
    private List<Ticket> tickets = new ArrayList<>();

    @ManyToOne
    private BoardType boardType;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "board")
    private List<TableList> tableLists = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "board")
    private List<RoleManager> roleManagers = new ArrayList<>();
}