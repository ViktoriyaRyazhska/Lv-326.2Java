package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;
import com.softserve.edu.cajillo.entity.enums.SprintStatus;

import java.util.List;

public interface SprintService {

    Sprint getSprint(Long id);

    void createSprint(SprintDto sprintDto);

    void createSprintBacklog(SprintDto sprintDto);

    List<Sprint> getAllSprintsByBoardId(Long boardId);

    List<Sprint> getAllSprintsByBoardAndStatusInProgress(Long boardId, SprintStatus sprintStatus);

    List<Sprint> getAllSprintsByBoardAndStatusCompleted(Long boardId, SprintStatus sprintStatus);

    void updateSprint(Long boardId, SprintDto sprintDto);

    void deleteSprint(Long sprintId);

    void archiveSprint(Long sprintId);

}
