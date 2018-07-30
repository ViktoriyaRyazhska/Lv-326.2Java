package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.*;
import com.softserve.edu.cajillo.entity.enums.TicketIssueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class GetSingleTicketResponse extends BaseDto {

    public GetSingleTicketResponse(Long id, String name, String description, String priority, Long assignedTo, Instant createTime, Instant updateTime, Instant expirationDate, Long tableListOId, Long boardId, String status, Long backlogId, Long sprintId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.assignedTo = assignedTo;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.expirationDate = expirationDate;
        this.tableListOId = tableListOId;
        this.boardId = boardId;
        this.status = status;
        this.backlogId = backlogId;
        this.sprintId = sprintId;
    }

    private Long id;

    private String name;

    private String description;

    private String priority;

//    private String ticketIssueType;

    private Long assignedTo;

    private Instant createTime;

    private Instant updateTime;

    private Instant expirationDate;

    private Long tableListOId;

    private Long boardId;

    private String status;
  
//    private Status status;


//    private List<Comment> comments;

    private Long backlogId;

    private Long sprintId;

    @Override
    public String toString() {
        return "GetSingleTicketResponse{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", priority='" + priority + '\'' +
////                ", ticketIssueType='" + ticketIssueType + '\'' +
////                ", AssignedTo=" + AssignedTo +
//                ", expirationDate=" + expirationDate +
//                ", tableListOId=" + tableListOId +
//                ", boardId=" + boardId +
//                ", status=" + status +
//                ", backlogId=" + backlogId +
//                ", sprintId=" + sprintId +
                '}';
    }
}