package com.softserve.edu.cajillo.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDto extends BaseDto {
    private Long id;
    private String name;
    private String boardType;
    private String image;
    private String imageName;
    private List<TableListDto> tableLists;
    private List<SprintDto> sprints;
    private List<HistoryLogDto> logs;
}