package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.OrderSprintDto;
import com.softserve.edu.cajillo.dto.SprintDto;

import java.util.List;

public interface SprintService {

    SprintDto getSprint(Long id);

    SprintDto getSprintBacklog(Long sprintId);

    SprintDto createSprint(SprintDto sprintDto, Long boardid);

    SprintDto createSprintBacklog(Long boardId);

    List<SprintDto> getAllSprintsByBoardIdNotInArchive(Long boardId);

    SprintDto updateSprint(Long boardId, SprintDto sprintDto);

    SprintDto archiveSprint(Long sprintId);

    SprintDto recoverSprint(Long sprintId);

    void archiveAllSprintsByBoard(Long boardId);

    List<SprintDto> recoverAllSprintsByBoard(Long boardId);

    void deleteSprint(Long sprintId);

    void decrementNextSprint(Long boardId, Long sprintId);

    void updateSprintSequenceNumber(OrderSprintDto orderSprintDto);

    List<SprintDto> sortSprintsBySequenceNumber(List<SprintDto> sprintDto);
}