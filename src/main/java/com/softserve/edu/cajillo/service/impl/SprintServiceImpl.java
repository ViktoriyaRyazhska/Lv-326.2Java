package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.impl.SprintConverterImpl;
import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;
import com.softserve.edu.cajillo.entity.enums.SprintStatus;
import com.softserve.edu.cajillo.entity.enums.SprintType;
import com.softserve.edu.cajillo.exception.SprintNotFoundException;
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
    SprintConverterImpl sprintConverter;

    @Override
    public Sprint getSprint(Long sprintId) {
        return sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
    }

    @Override
    public void createSprint(SprintDto sprintDto) {
        sprintDto.setSprintType(SprintType.SPRINT);
        sprintDto.setSprintStatus(SprintStatus.IN_PROGRESS);
        sprintRepository.save(sprintConverter.convertToEntity(sprintDto));
    }

    public void createSprintBacklog(SprintDto sprintDto){
        sprintDto.setSprintType(SprintType.BACKLOG);
        sprintDto.setSprintStatus(SprintStatus.IN_PROGRESS);
        sprintRepository.save(sprintConverter.convertToEntity(sprintDto));
    }

    @Override
    public List<Sprint> getAllSprintsByBoardId(Long boardId) {
        return sprintRepository.getAllByBoardId(boardId)
                        .orElseThrow(() -> new SprintNotFoundException(SPRINTS_NOT_FOUND_MESSAGE + boardId));
    }

    @Override
    public List<Sprint> getAllSprintsByBoardAndStatusInProgress(Long boardId, SprintStatus sprintStatus) {
        return sprintRepository.getAllByBoardAndSprintStatus(boardId, SprintStatus.IN_PROGRESS)
                .orElseThrow(() -> new SprintNotFoundException(SPRINTS_NOT_FOUND_MESSAGE + boardId));
    }

    @Override
    public List<Sprint> getAllSprintsByBoardAndStatusCompleted(Long boardId, SprintStatus sprintStatus) {
        return sprintRepository.getAllByBoardAndSprintStatus(boardId, SprintStatus.COMPLETED)
                .orElseThrow(() -> new SprintNotFoundException(SPRINTS_NOT_FOUND_MESSAGE + boardId));
    }

    @Override
    public void deleteSprint(Long sprintId) {
       sprintRepository.delete(
                sprintRepository.findById(sprintId)
                        .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId)));
    }

    @Override
    public void updateSprint(Long sprintId, SprintDto updatedSprintDto) {
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        Sprint updatedSprint = sprintConverter.convertToEntity(updatedSprintDto);
        currentSprint.setLabel(updatedSprint.getLabel());
        currentSprint.setStartDate(updatedSprint.getStartDate());
        currentSprint.setEndDate(updatedSprint.getEndDate());
        currentSprint.setGoal(updatedSprint.getGoal());
        currentSprint.setBoard(updatedSprint.getBoard());
        currentSprint.setSprintType(updatedSprint.getSprintType());
        currentSprint.setSprintStatus(updatedSprint.getSprintStatus());
        sprintRepository.save(currentSprint);
    }

    public void archiveSprint(Long sprintId){
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        currentSprint.setSprintStatus(SprintStatus.IN_ARCHIVE);
        sprintRepository.save(currentSprint);
    }
}
