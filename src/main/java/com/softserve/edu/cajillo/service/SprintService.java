package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;

import java.util.List;

public interface SprintService {

    Sprint getSprint(Long id);

    void createSprint(SprintDto sprintDto);

    void createSprintBacklog(SprintDto sprintDto);

    List<Sprint> getAllSprintsByBoard(Long boardId);

    void updateSprint(Long boardId, SprintDto sprintDto);

    void deleteSprint(Long listId);

}
