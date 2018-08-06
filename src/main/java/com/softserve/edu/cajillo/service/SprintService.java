package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.SprintDto;

import java.util.List;

public interface SprintService {

    SprintDto getSprint(Long id);

    void createSprint(SprintDto sprintDto);

    void createSprintBacklog(Long boardId);

    List<SprintDto> getAllSprintsByBoardIdNotInArchive
            (Long boardId);

    List<SprintDto> getAllSprintsByBoardAndStatusCreated(Long boardId);

    List<SprintDto> getAllSprintsByBoardAndStatusInProgress(Long boardId);

    List<SprintDto> getAllSprintsByBoardAndStatusCompleted(Long boardId);

    void updateSprint(Long boardId, SprintDto sprintDto);

    void archiveSprint(Long sprintId);

    SprintDto recoverSprint(Long sprintId);

    void archiveAllSprintsByBoard(Long boardId);

    void recoverAllSprintsByBoard(Long boardId);

    void deleteSprint(Long sprintId);

}