package com.softserve.edu.cajillo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
//import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity<I> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private I id;

}
