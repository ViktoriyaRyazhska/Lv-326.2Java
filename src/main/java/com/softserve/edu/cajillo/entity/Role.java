package com.softserve.edu.cajillo.entity;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    private RoleName name;

    @OneToOne(mappedBy = "role",
            fetch = FetchType.LAZY)
    private RoleManager roleManager;
}