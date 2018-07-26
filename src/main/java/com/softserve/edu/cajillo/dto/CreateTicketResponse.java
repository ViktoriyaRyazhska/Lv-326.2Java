package com.softserve.edu.cajillo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateTicketResponse {

    @NotBlank
    private Long ticketId;

    @NotBlank
    private Long tableListId;

    @NotBlank
    private Long boardId;

    public CreateTicketResponse() {
    }

    public CreateTicketResponse(@NotBlank Long ticketId, @NotBlank Long tableListId, @NotBlank Long boardId) {
        this.ticketId = ticketId;
        this.tableListId = tableListId;
        this.boardId = boardId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
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

    @Override
    public String toString() {
        return "CreateTicketResponse{" +
                "ticketId=" + ticketId +
                ", tableListId=" + tableListId +
                ", boardId=" + boardId +
                '}';
    }
}
