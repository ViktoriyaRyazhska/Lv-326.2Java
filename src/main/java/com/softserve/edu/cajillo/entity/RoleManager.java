package com.softserve.edu.cajillo.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "board_user_role")
@Data
public class RoleManager extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    @ManyToOne
    private User user;

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    private Team team;
}
