package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.TableList;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
public class CreateTicketRequest {

    @NotBlank
    private String name;

    @NotBlank
    private Long tableListId;

    @NotBlank
    private Long boardId;

    public CreateTicketRequest() {
    }

    public CreateTicketRequest(@NotBlank String name, @NotBlank Long tableListId, @NotBlank Long boardId) {
        this.name = name;
        this.tableListId = tableListId;
        this.boardId = boardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTableListId() {
        return tableListId;
    }

    public void setTableListId(Long tableListId) {
        this.tableListId = tableListId;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
}
