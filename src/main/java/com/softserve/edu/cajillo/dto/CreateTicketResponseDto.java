package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketResponseDto extends BaseDto {

    private String name;

    @NotBlank
    private Long ticketId;

    @NotBlank
    private Long tableListId;

    @NotBlank
    private Long boardId;

}