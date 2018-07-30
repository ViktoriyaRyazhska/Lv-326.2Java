package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableListDto extends BaseDto{
    private Long id;
    private String name;
    private Instant createTime;
    private Instant updateTime;
    private Long boardId;
    private String boardName;
//    private List<Ticket> tickets;

}
