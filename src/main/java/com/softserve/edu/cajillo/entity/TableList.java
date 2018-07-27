package com.softserve.edu.cajillo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "table_lists")
@EqualsAndHashCode(callSuper = false)
@NamedEntityGraph(name = "TableList.board",
        attributeNodes = @NamedAttributeNode("board"))
@ToString(exclude = "board")
public class TableList extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

//    @CreatedDate
//    @Column(name = "create_time")
//    private Instant createTime;
//
//    @LastModifiedDate
//    private Instant updateTime;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Board board;

    @OneToOne(fetch = FetchType.LAZY)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "tableList")
    private List<Ticket> tickets = new ArrayList<>();

}