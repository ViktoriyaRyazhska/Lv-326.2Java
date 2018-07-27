package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.RoleName;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role")
    private RoleManager roleManager;

    public Role() {
    }

    public Role(RoleName name) {
        this.name = name;
    }
}