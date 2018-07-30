package com.softserve.edu.cajillo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private String message;

    private Long userId;

    private String status;

    private Long ticketId;

    @Override
    public String toString() {
        return "CommentResponse{" +
//                "message='" + message + '\'' +
//                ", userId=" + userId +
//                ", status='" + status + '\'' +
//                ", ticketId=" + ticketId +
                '}';
    }
}
