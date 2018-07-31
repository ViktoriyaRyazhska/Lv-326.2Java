package com.softserve.edu.cajillo.dto;

import lombok.Data;

@Data
public class BoardDto extends BaseDto {
    private Long boardId;
    private String name;
}