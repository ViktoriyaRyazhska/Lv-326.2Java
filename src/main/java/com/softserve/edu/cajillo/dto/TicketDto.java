package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto extends BaseDto {

    private Long id;

    private Instant createTime;

    private Instant updateTime;

    private String description;

    private Instant expirationDate;

    private String name;

    private String ticketPriority;

    private Long assignedToId;

    private Long createdById;

    private Long boardId;

    private Long sprintId;

    private Long tableListId;

    private List<CommentDto> comments;
}