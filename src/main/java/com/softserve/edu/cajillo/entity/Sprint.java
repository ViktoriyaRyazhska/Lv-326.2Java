package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.SprintStatus;
import com.softserve.edu.cajillo.entity.enums.SprintType;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "sprints")
public class Sprint extends DateAudit{

    @Column(name = "label", nullable = false, length = 150)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "sprint_status", nullable = false)
    private SprintStatus sprintStatus;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;
}