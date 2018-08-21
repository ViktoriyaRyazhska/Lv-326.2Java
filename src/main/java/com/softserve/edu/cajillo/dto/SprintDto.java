package com.softserve.edu.cajillo.dto;

import com.softserve.edu.cajillo.entity.enums.SprintStatus;
import com.softserve.edu.cajillo.entity.enums.SprintType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  SprintDto extends BaseDto{

    private Long id;

    private String label;

    private Instant startDate;

    private Instant endDate;

    private String goal;

    private Long boardId;

    private SprintType sprintType;

    private SprintStatus sprintStatus;

    private Integer sequenceNumber;

    private List<TicketForBoardResponseDto> ticketsForBoardResponse;
}
