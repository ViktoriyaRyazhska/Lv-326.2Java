package com.softserve.edu.cajillo.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board_user_role")
@Data
public class RoleManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "roleManagers")
    private List<Board> boards = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "roleManagers")
    private List<User> users = new ArrayList<>();

    @OneToOne(mappedBy = "roleManager",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Role role;
}
