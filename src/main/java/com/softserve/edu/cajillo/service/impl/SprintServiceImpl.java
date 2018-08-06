package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.impl.SprintConverterImpl;
import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;
import com.softserve.edu.cajillo.entity.enums.BoardType;
import com.softserve.edu.cajillo.entity.enums.SprintStatus;
import com.softserve.edu.cajillo.entity.enums.SprintType;
import com.softserve.edu.cajillo.exception.BacklogModificationException;
import com.softserve.edu.cajillo.exception.BoardTypeMismatchException;
import com.softserve.edu.cajillo.exception.SprintNotFoundException;
import com.softserve.edu.cajillo.repository.SprintRepository;
import com.softserve.edu.cajillo.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprintServiceImpl implements SprintService {

    private static final String SPRINT_ID_NOT_FOUND_MESSAGE = "Could not find sprint with id=";
    private static final String SPRINT_ID_FOUND_IN_ARCHIVE = "Find in archive sprint or backlog with id=";
    private static final String SPRINT_FOR_CUSTOM_BOARD_MISMATCH = "Could not create sprint for custom board with id=";
    private static final String BACKLOG_DELETE_IS_PROHIBITED = "Prohibited to delete sprint backlog";
    private static final String BACKLOG_UPDATE_IS_PROHIBITED = "Prohibited to modificate sprint backlog";
    private static final String BACKLOG_RECOVERY_IS_PROHIBITED = "Prohibited to recover sprint backlog";

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    SprintConverterImpl sprintConverter;

    @Override
    public SprintDto getSprint(Long sprintId) {
        Sprint foundSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        if(foundSprint.getSprintStatus().equals(SprintStatus.IN_ARCHIVE)){
            throw new SprintNotFoundException(SPRINT_ID_FOUND_IN_ARCHIVE + sprintId);
        } else {
            return sprintConverter.convertToDto(foundSprint);
        }
    }

    @Override
    public void createSprint(SprintDto sprintDto, Long boardId) {
        sprintDto.setBoardId(boardId);
        if(sprintConverter.convertToEntity(sprintDto).getBoard().getBoardType()
                .equals(BoardType.CUSTOM)){
            throw new BoardTypeMismatchException(SPRINT_FOR_CUSTOM_BOARD_MISMATCH +sprintDto.getBoardId());
        }
        sprintDto.setSprintType(SprintType.SPRINT);
        sprintDto.setSprintStatus(SprintStatus.CREATED);
        sprintRepository.save(
                sprintConverter.convertToEntity(sprintDto));
    }

    @Override
    public void createSprintBacklog(Long boardId){
        SprintDto backlogDto = new SprintDto();
        backlogDto.setBoardId(boardId);
        backlogDto.setLabel("Backlog");
        backlogDto.setSprintType(SprintType.BACKLOG);
        backlogDto.setSprintStatus(SprintStatus.CREATED);
        sprintRepository.save(
                sprintConverter.convertToEntity(backlogDto));
    }

    @Override
    public List<SprintDto> getAllSprintsByBoardIdNotInArchive(Long boardId) {
        return sprintConverter.convertToDto(sprintRepository.getAllByBoardIdAndSprintStatusNot
                (boardId, SprintStatus.IN_ARCHIVE));
    }

    @Override
    public List<SprintDto> getAllSprintsByBoardAndStatusCreated(Long boardId) {
        return sprintConverter.convertToDto(sprintRepository.getAllByBoardIdAndSprintStatus(boardId, SprintStatus.CREATED));
    }

    @Override
    public List<SprintDto> getAllSprintsByBoardAndStatusInProgress(Long boardId) {
        return sprintConverter.convertToDto(sprintRepository.getAllByBoardIdAndSprintStatus(boardId, SprintStatus.IN_PROGRESS));
    }

    @Override
    public List<SprintDto> getAllSprintsByBoardAndStatusCompleted(Long boardId) {
        return sprintConverter.convertToDto(sprintRepository.getAllByBoardIdAndSprintStatus(boardId, SprintStatus.COMPLETED));
    }

    @Override
    public void updateSprint(Long sprintId, SprintDto updatedSprintDto) {
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        if(currentSprint.getSprintType().equals(SprintType.BACKLOG)){
            throw new BacklogModificationException(BACKLOG_UPDATE_IS_PROHIBITED);
        }
        currentSprint.setSprintType(SprintType.SPRINT);
        Sprint updatedSprint = sprintConverter.convertToEntity(updatedSprintDto);
        currentSprint.setLabel(updatedSprint.getLabel());
        currentSprint.setStartDate(updatedSprint.getStartDate());
        currentSprint.setEndDate(updatedSprint.getEndDate());
        currentSprint.setGoal(updatedSprint.getGoal());
        currentSprint.setBoard(updatedSprint.getBoard());
        currentSprint.setSprintStatus(updatedSprint.getSprintStatus());
        sprintRepository.save(currentSprint);
    }

    @Override
    public void archiveSprint(Long sprintId){
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        if(currentSprint.getSprintType().equals(SprintType.BACKLOG)){
            throw new BacklogModificationException(BACKLOG_DELETE_IS_PROHIBITED);
        }
        currentSprint.setSprintStatus(SprintStatus.IN_ARCHIVE);
        sprintRepository.save(currentSprint);
    }

    @Override
    public SprintDto recoverSprint(Long sprintId){
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        if(currentSprint.getSprintType().equals(SprintType.BACKLOG)){
            throw new BacklogModificationException(BACKLOG_RECOVERY_IS_PROHIBITED);
        }
        currentSprint.setSprintStatus(SprintStatus.CREATED);
        sprintRepository.save(currentSprint);
        return sprintConverter.convertToDto(currentSprint);
    }

    @Override
    public void archiveAllSprintsByBoard(Long boardId){
        List<Sprint> sprintList = sprintRepository.getAllByBoardIdAndSprintStatusNot
                (boardId, SprintStatus.IN_ARCHIVE);
        for(Sprint item: sprintList){
            item.setSprintStatus(SprintStatus.IN_ARCHIVE);
            sprintRepository.save(item);
        }
    }

    @Override
    public void recoverAllSprintsByBoard(Long boardId){
        List<Sprint> sprintList = sprintRepository.getAllByBoardIdAndSprintStatus(boardId, SprintStatus.IN_ARCHIVE);
        for(Sprint item: sprintList){
            item.setSprintStatus(SprintStatus.CREATED);
            sprintRepository.save(item);
        }
    }

    @Override
    public void deleteSprint(Long sprintId) {
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        if(currentSprint.getSprintType().equals(SprintType.BACKLOG)){
            throw new BacklogModificationException(BACKLOG_DELETE_IS_PROHIBITED);
        }
        sprintRepository.delete(currentSprint);
    }

}