package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.TableList;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class TableListDto {
    private Long id;
    private String name;
    private Instant createTime;
    private Instant updateTime;
    private Long boardId;
    private String boardName;

}
