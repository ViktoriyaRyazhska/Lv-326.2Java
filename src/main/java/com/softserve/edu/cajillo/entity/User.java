package com.softserve.edu.cajillo.entity;

import lombok.Data;


import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String avatar;
    private String roleId;
    private java.sql.Timestamp createTime;
}