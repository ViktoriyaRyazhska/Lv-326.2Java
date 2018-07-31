package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSingleTicketResponseDto extends BaseDto {

    private Long id;

    private Instant createTime;

    private Instant updateTime;

    private String description;

    private Instant expirationDate;

    private String name;

    private String priority;

    private Long assignedTo;

    private Long createdBy;

    private Long boardId;

    private Long sprintId;

    private Long tableListId;
}