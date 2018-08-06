package com.softserve.edu.cajillo.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketRequestDto extends BaseDto {

    @NotBlank
    private String name;

    @NotBlank
    private Long tableListId;

    @NotBlank
    private Long boardId;

    private Long createdById ;
}