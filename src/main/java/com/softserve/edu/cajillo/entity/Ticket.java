package com.softserve.edu.cajillo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;
    private String name;
    private long listId;
    private String statusId;
    private String priority;
    private String createTime;
    private String updateTime;
    private String expirationDate;
}