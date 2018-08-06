package com.softserve.edu.cajillo.entity;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class BaseEntity<I> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private I id;
}