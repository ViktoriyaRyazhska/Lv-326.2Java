package com.softserve.edu.cajillo.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class HistoryLogDto extends BaseDto{
    private Long id;
    private Long boardId;
    private Long userId;
    private String username;
    private String message;
    private Instant createTime;
}
