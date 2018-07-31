package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketResponseDto {

    @NotBlank
    private Long ticketId;

    @NotBlank
    private Long tableListId;

    @NotBlank
    private Long boardId;
}