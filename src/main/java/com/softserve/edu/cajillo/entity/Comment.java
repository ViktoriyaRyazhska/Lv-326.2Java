package com.softserve.edu.cajillo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;
    private String message;
    private long userId;
    private long statusId;
    private long ticketId;
    private String createTime;
    private String updateTime;
}