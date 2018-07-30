package com.softserve.edu.cajillo.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "sprints")
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;

    private Instant startDate;
    private Instant endDate;

    //long description about the goal of sprint (length 200)
    private String goal;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "sprint")
    private List<Ticket> tickets = new ArrayList<>();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Backlog backlog;

    @Override
    public String toString() {
        return "Sprint{" +
//                "id=" + id +
//                ", label='" + label + '\'' +
//                ", startDate=" + startDate +
//                ", endDate=" + endDate +
//                ", goal='" + goal + '\'' +
//                ", tickets=" + tickets +
//                ", backlog=" + backlog +
                '}';
    }
}
