package com.softserve.edu.cajillo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "scrum_backlogs")
public class Backlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "backlog")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "backlog")
    private List<Sprint> sprints = new ArrayList<>();

    @Override
    public String toString() {
        return "Backlog{" +
                "id=" + id +
                ", label='" + label + '\'' +
//                ", tickets=" + tickets +
//                ", sprints=" + sprints +
                '}';
    }
}