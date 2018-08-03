package com.softserve.edu.cajillo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "teams")
@Data
public class Team extends DateAudit {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;
}