package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "table_lists")
@EqualsAndHashCode(callSuper = false)
public class TableList extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Board board;

    @Enumerated(EnumType.STRING)
    private ItemsStatus status;

    private Integer sequenceNumber;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "tableList")
    private List<Ticket> tickets = new ArrayList<>();

    @Override
    public String toString() {
        return "TableList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sequenceNumber=" + sequenceNumber +
//                ", board=" + board +
//                ", status=" + status +
//                ", tickets=" + tickets +
                '}';
    }
}