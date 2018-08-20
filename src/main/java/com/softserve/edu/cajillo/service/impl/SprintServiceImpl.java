package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.impl.SprintConverterImpl;
import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.enums.BoardType;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.entity.enums.SprintStatus;
import com.softserve.edu.cajillo.entity.enums.SprintType;
import com.softserve.edu.cajillo.exception.*;
import com.softserve.edu.cajillo.repository.SprintRepository;
import com.softserve.edu.cajillo.service.BoardService;
import com.softserve.edu.cajillo.service.SprintService;
import com.softserve.edu.cajillo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SprintServiceImpl implements SprintService {

    private static final String BOARD_ID_NULL = "Could not find board with id=null";
    private static final String SPRINT_ID_NULL = "Could not find sprint with id=null";
    private static final String SPRINT_ID_NOT_FOUND_MESSAGE = "Could not find sprint with id=";
    private static final String SPRINT_ID_FOUND_IN_ARCHIVE = "Find in archive sprint or backlog with id=";
    private static final String SPRINT_FOR_CUSTOM_BOARD_MISMATCH = "Could not create sprint for custom board with id=";
    private static final String BACKLOG_DELETE_IS_PROHIBITED = "Prohibited to delete sprint backlog";
    private static final String BACKLOG_UPDATE_IS_PROHIBITED = "Prohibited to modificate sprint backlog";
    private static final String BACKLOG_RECOVERY_IS_PROHIBITED = "Prohibited to recover sprint backlog";
    private static final String SPRINT_RECOVERY_IS_PROHIBITED = "Prohibited to recover sprint with board in archive with id=";

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private SprintConverterImpl sprintConverter;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private BoardService boardService;

    @Override
    public SprintDto getSprint(Long sprintId) {
        if(sprintId == null){
            throw new SprintNotFoundException(SPRINT_ID_NULL);
        }
        Sprint foundSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        if(foundSprint.getSprintStatus().equals(SprintStatus.IN_ARCHIVE)){
            throw new SprintNotFoundException(SPRINT_ID_FOUND_IN_ARCHIVE + sprintId);
        } else {
            return sprintConverter.convertToDto(foundSprint);
        }
    }

    @Override
    public SprintDto getSprintBacklog(Long boardId) {
        if(boardId == null){
            throw new BoardNotFoundException(BOARD_ID_NULL);
        }
        boardService.getBoardEntity(boardId);
        return sprintConverter.convertToDto
                (sprintRepository.getByBoardIdAndSprintType(boardId, SprintType.BACKLOG));
    }

    @Override
    public SprintDto createSprint(SprintDto sprintDto, Long boardId) {
        if(boardId == null){
            throw new BoardNotFoundException(BOARD_ID_NULL);
        }
        sprintDto.setBoardId(boardId);
        if(sprintConverter.convertToEntity(sprintDto).getBoard().getBoardType()
                .equals(BoardType.CUSTOM)){
            throw new BoardTypeMismatchException(SPRINT_FOR_CUSTOM_BOARD_MISMATCH +sprintDto.getBoardId());
        }
        sprintDto.setSprintType(SprintType.SPRINT);
        sprintDto.setSprintStatus(SprintStatus.CREATED);
        Long maxSequenceValue = sprintRepository.getMaxSequenceValue(boardId);
        sprintDto.setSequenceNumber(Math.toIntExact((maxSequenceValue == null) ? 1 : ++maxSequenceValue));
        return sprintConverter.convertToDto(sprintRepository.save(
                sprintConverter.convertToEntity(sprintDto)));
    }

    @Override
    public void createSprintBacklog(Long boardId){
        if(boardId == null){
            throw new BoardNotFoundException(BOARD_ID_NULL);
        }
        SprintDto backlogDto = new SprintDto();
        backlogDto.setBoardId(boardId);
        backlogDto.setLabel("Backlog");
        backlogDto.setSprintType(SprintType.BACKLOG);
        backlogDto.setSprintStatus(SprintStatus.CREATED);
        backlogDto.setSequenceNumber(null);
        sprintRepository.save(
                sprintConverter.convertToEntity(backlogDto));
    }

    @Override
    public List<SprintDto> getAllSprintsByBoardIdNotInArchive(Long boardId) {
        if(boardId == null){
            throw new BoardNotFoundException(BOARD_ID_NULL);
        }
        return sprintConverter.convertToDto(sprintRepository.getAllByBoardIdAndSprintStatusNotAndSprintType
                (boardId, SprintStatus.IN_ARCHIVE, SprintType.SPRINT));
    }

    /*
    methods for getting sprints in statuses: Created, In Progress, Completed
    */
    @Override
    public List<SprintDto> getAllSprintsByBoardAndStatusCreated(Long boardId) {
        if(boardId == null){
            throw new BoardNotFoundException(BOARD_ID_NULL);
        }
        return sprintConverter.convertToDto(
                sprintRepository.getAllByBoardIdAndSprintStatusNotAndSprintType
                        (boardId, SprintStatus.CREATED, SprintType.SPRINT));
    }

    @Override
    public List<SprintDto> getAllSprintsByBoardAndStatusInProgress(Long boardId) {
        if(boardId == null){
            throw new BoardNotFoundException(BOARD_ID_NULL);
        }
        return sprintConverter.convertToDto(
                sprintRepository.getAllByBoardIdAndSprintStatusNotAndSprintType
                        (boardId, SprintStatus.IN_PROGRESS, SprintType.SPRINT));
    }

    @Override
    public List<SprintDto> getAllSprintsByBoardAndStatusCompleted(Long boardId) {
        if(boardId == null){
            throw new BoardNotFoundException(BOARD_ID_NULL);
        }
        return sprintConverter.convertToDto(
                sprintRepository.getAllByBoardIdAndSprintStatusNotAndSprintType
                        (boardId, SprintStatus.COMPLETED, SprintType.SPRINT));
    }

    @Override
    public SprintDto updateSprint(Long sprintId, SprintDto updatedSprintDto) {
        if(sprintId == null){
            throw new SprintNotFoundException(SPRINT_ID_NULL);
        }
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
        currentSprint.setSequenceNumber(updatedSprint.getSequenceNumber());
        return sprintConverter.convertToDto(sprintRepository.save(currentSprint));
    }

    @Override
    public void deleteSprint(Long sprintId) {
        if(sprintId == null){
            throw new SprintNotFoundException(SPRINT_ID_NULL);
        }
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        if(currentSprint.getSprintType().equals(SprintType.BACKLOG)){
            throw new BacklogModificationException(BACKLOG_DELETE_IS_PROHIBITED);
        }
        sprintRepository.delete(currentSprint);
    }

    /*
    methods for archive function and recovery
    */
    @Override
    public SprintDto archiveSprint(Long sprintId){
        if(sprintId == null){
            throw new SprintNotFoundException(SPRINT_ID_NULL);
        }
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        if(currentSprint.getSprintType().equals(SprintType.BACKLOG)){
            throw new BacklogModificationException(BACKLOG_DELETE_IS_PROHIBITED);
        }
        decrementNextSprint(currentSprint.getBoard().getId(), sprintId);
        currentSprint.setSequenceNumber(null);
        currentSprint.setSprintStatus(SprintStatus.IN_ARCHIVE);
        ticketService.archiveTicketsBySprintId(sprintId);
        return sprintConverter.convertToDto(sprintRepository.save(currentSprint));

    }

    @Override
    public SprintDto recoverSprint(Long sprintId){
        if(sprintId == null){
            throw new SprintNotFoundException(SPRINT_ID_NULL);
        }
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        if(currentSprint.getSprintType().equals(SprintType.BACKLOG)){
            throw new BacklogModificationException(BACKLOG_RECOVERY_IS_PROHIBITED);
        }
        if(currentSprint.getBoard().getStatus().equals(ItemsStatus.DELETED)){
            throw new BacklogModificationException(SPRINT_RECOVERY_IS_PROHIBITED + currentSprint.getBoard().getId());
        }
        currentSprint.setSprintStatus(SprintStatus.CREATED);
        ticketService.recoverTicketsBySprintId(sprintId);
        return sprintConverter.convertToDto(sprintRepository.save(currentSprint));
    }

    @Override
    public void archiveAllSprintsByBoard(Long boardId){
        if(boardId == null){
            throw new BoardNotFoundException(BOARD_ID_NULL);
        }
        List<Sprint> sprintList = sprintRepository.getAllByBoardIdAndSprintStatusNot
                (boardId, SprintStatus.IN_ARCHIVE);
        for(Sprint sprint: sprintList){
            sprint.setSprintStatus(SprintStatus.IN_ARCHIVE);
            sprintRepository.save(sprint);
        }
    }

    @Override
    public  List<SprintDto> recoverAllSprintsByBoard(Long boardId){
        if(boardId == null){
            throw new BoardNotFoundException(BOARD_ID_NULL);
        }
        List<Sprint> sprintList = sprintRepository.getAllByBoardIdAndSprintStatusNot
                (boardId, SprintStatus.IN_ARCHIVE);
        List<SprintDto> result = new ArrayList<>();
        for(Sprint sprint: sprintList){
            sprint.setSprintStatus(SprintStatus.CREATED);
            result.add(sprintConverter.convertToDto(sprintRepository.save(sprint)));

        }
        return result;
    }

    /*
    * sequence number of Sprint - for swap functionality
    */

    public void decrementNextSprint(Long boardId, Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId));
        List<Sprint> sprints = sprintRepository
                .findByBoardIdAndSequenceNumberGreaterThan(boardId, sprint.getSequenceNumber());
        for (Sprint s: sprints) {
            s.setSequenceNumber(s.getSequenceNumber() - 1);
        }
    }

    public List<SprintDto> swapSequenceNumbers(Long sprintId1, Long sprintId2) {
        Sprint sprint1 = sprintRepository.findById(sprintId1)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId1));
        Sprint sprint2 = sprintRepository.findById(sprintId2)
                .orElseThrow(() -> new SprintNotFoundException(SPRINT_ID_NOT_FOUND_MESSAGE + sprintId2));
        swapNumbers(sprint1, sprint2);
        sprintRepository.save(sprint1);
        sprintRepository.save(sprint1);
        List<Sprint> sprints = sprintRepository.getAllByBoardIdAndSprintStatusNotAndSprintType
                (sprint1.getBoard().getId(), SprintStatus.IN_ARCHIVE, SprintType.SPRINT);
        return sprintConverter.convertToDto(sprints);
    }

    public void swapNumbers(Sprint sprint1, Sprint sprint2) {
        int number = sprint1.getSequenceNumber();
        sprint1.setSequenceNumber(sprint1.getSequenceNumber());
        sprint2.setSequenceNumber(number);
    }

}