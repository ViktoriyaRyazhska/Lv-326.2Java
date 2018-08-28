package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketDto extends BaseDto {

    @NotBlank
    private String name;

    @NotBlank
    private Long id;

    @NotBlank
    private Long tableListId;

    @NotBlank
    private Long boardId;

    private Long createdById ;

    private Long sprintId;

    @NotBlank
    private Long sequenceNumber;
}