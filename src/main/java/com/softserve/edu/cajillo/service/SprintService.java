package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.SprintDto;

import java.util.List;

public interface SprintService {

    SprintDto getSprint(Long id);

    SprintDto getSprintBacklog(Long sprintId);

    SprintDto createSprint(SprintDto sprintDto, Long boardid);

    void createSprintBacklog(Long boardId);

    List<SprintDto> getAllSprintsByBoardIdNotInArchive(Long boardId);

    List<SprintDto> getAllSprintsByBoardAndStatusCreated(Long boardId);

    List<SprintDto> getAllSprintsByBoardAndStatusInProgress(Long boardId);

    List<SprintDto> getAllSprintsByBoardAndStatusCompleted(Long boardId);

    SprintDto updateSprint(Long boardId, SprintDto sprintDto);

    SprintDto archiveSprint(Long sprintId);

    SprintDto recoverSprint(Long sprintId);

    void archiveAllSprintsByBoard(Long boardId);

    List<SprintDto> recoverAllSprintsByBoard(Long boardId);

    void deleteSprint(Long sprintId);

}