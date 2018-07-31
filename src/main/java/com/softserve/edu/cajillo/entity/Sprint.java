package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.SprintType;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "sprints")
public class Sprint extends DateAudit{

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "goal")
    private String goal;

    @ManyToOne
    @JoinColumn(name = "board_id",  nullable = false)
    private Board board;

    @Enumerated(EnumType.STRING)
    @Column(name = "sprint_type", nullable = false)
    private SprintType sprintType;
}