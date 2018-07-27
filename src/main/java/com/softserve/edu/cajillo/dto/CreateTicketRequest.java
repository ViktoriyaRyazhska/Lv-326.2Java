package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.TableList;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketRequest {

    @NotBlank
    private String name;

    @NotBlank
    private Long tableListId;

    @NotBlank
    private Long boardId;
}
