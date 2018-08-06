package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.enums.CommentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto extends BaseDto {

    private Long commentId;

    private String message;

    private Long userId;

    private CommentStatus commentStatus;

    private Long ticketId;

    private Instant createTime;

    private Instant updateTime;
}