package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.impl.SprintConverterImpl;
import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;
import com.softserve.edu.cajillo.exception.SprintNotFoundException;
import com.softserve.edu.cajillo.exception.UnsatisfiedException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.SprintRepository;
import com.softserve.edu.cajillo.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprintServiceImpl implements SprintService {

    private static final String SPRINT_ID_NOT_FOUND_MESSAGE = "Could not find sprint with id=";
    private static final String SPRINTS_NOT_FOUND_MESSAGE = "Could not find sprints by board with id=";


    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    SprintConverterImpl sprintConverter;

    @Override
    public SprintDto createSprint(SprintDto createSprintDto) {
        Sprint sprint = new Sprint();
        sprint.setLabel(createSprintDto.getLabel());
        sprint.setStartDate(createSprintDto.getStartDate());
        sprint.setEndDate(createSprintDto.getEndDate());
        sprint.setGoal(createSprintDto.getGoal());
        sprint.setSprintType(createSprintDto.getSprintType());
        sprint.setSprintStatus(createSprintDto.getSprintStatus());
        sprint.setBoard(boardRepository.findById(createSprintDto.getBoardId())
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + createSprintDto.getBoardId())));
        return sprintConverter.convertToDto(sprint);
    }

    @Override
    public SprintDto getSprint(Long sprintId) {
        return sprintConverter.convertToDto(
                sprintRepository.findById(sprintId)
                        .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId)));
    }

    @Override
    public List<SprintDto> getAllSprints(Long boardId) {
        return sprintConverter.convertToDto(
                sprintRepository.getAllByBoardId(boardId)
                        .orElseThrow(() -> new SprintNotFoundException(SPRINTS_NOT_FOUND_MESSAGE + boardId)));
    }

    @Override
    public void deleteSprint(Long sprintId) {
       sprintRepository.delete(
                sprintRepository.findById(sprintId)
                        .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId)));

    }

    @Override
    public SprintDto updateTableList(Long sprintId, SprintDto sprintDtoNew) {
        SprintDto sprintDtoOld = sprintConverter.convertToDto(sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINTS_NOT_FOUND_MESSAGE + sprintId)));
        sprintDtoOld.setLabel(sprintDtoNew.getLabel());
        sprintDtoOld.setStartDate(sprintDtoNew.getStartDate());
        sprintDtoOld.setEndDate(sprintDtoNew.getEndDate());
        sprintDtoOld.setGoal(sprintDtoNew.getGoal());
        sprintDtoOld.setSprintType(sprintDtoNew.getSprintType());
        sprintDtoOld.setSprintStatus(sprintDtoNew.getSprintStatus());
        return sprintDtoNew;
    }
}
