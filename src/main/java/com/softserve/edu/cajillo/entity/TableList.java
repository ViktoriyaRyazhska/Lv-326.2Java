package com.softserve.edu.cajillo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "table_lists")
public class TableList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long listId;
    private String name;
    private long boardId;
    private long statusId;
    private String createTime;
    private String updateTime;
}