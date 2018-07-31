package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "table_lists")
@EqualsAndHashCode(callSuper = false)
public class TableList extends DateAudit {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "board_id",  nullable = false)
    private Board board;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ItemsStatus status;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;
}