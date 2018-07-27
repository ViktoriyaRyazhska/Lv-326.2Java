package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSingleTicketResponse extends BaseDto {

    private Long id;
    private String name;
    private String priority;

    private Instant expirationDate;

    private Instant createTime;

    private Instant updateTime;

    private Long IdTableList;

    private Long IdBoard;

    private Status status;

    private List<Comment> comments = new ArrayList<>();


}