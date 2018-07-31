package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.RoleName;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "board_user_role")
@Data
public class RoleManager extends BaseEntity<Long> {

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private RoleName roleName;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;
}