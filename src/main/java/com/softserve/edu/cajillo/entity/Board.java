package com.softserve.edu.cajillo.entity;

import com.softserve.edu.cajillo.entity.enums.BoardType;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "boards")
public class Board extends BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    @NaturalId
    private BoardType boardType;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "boards")
    private List<User> users = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ItemsStatus status;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "board")
    private List<Ticket> tickets = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "board")
    private List<TableList> tableLists = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<RoleManager> roleManagers = new ArrayList<>();

//    @OneToOne(fetch = FetchType.LAZY)
//    private Backlog backlog;


    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
//                ", boardType=" + boardType +
//                ", users=" + users +
//                ", status=" + status +
//                ", tickets=" + tickets +
//                ", tableLists=" + tableLists +
//                ", roleManagers=" + roleManagers +
                '}';
    }
}