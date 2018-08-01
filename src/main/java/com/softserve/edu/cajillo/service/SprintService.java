package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;

import java.util.List;

public interface SprintService {

    SprintDto createSprint(SprintDto sprintDto);

    SprintDto getSprint(Long id);

    List<SprintDto> getAllSprints(Long boardId);

    SprintDto updateTableList(Long sprintId, SprintDto sprintDto);

    void deleteSprint(Long listId);

}
