package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.SprintConverter;
import com.softserve.edu.cajillo.dto.OrderSprintDto;
import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SprintServiceImpl implements SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private SprintConverter sprintConverter;

    @Autowired
    private TicketService ticketService;

    @Override
    public SprintDto getSprint(Long sprintId) {
        return sprintConverter.convertToDto(sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint", "id", sprintId)));
    }

    @Override
    public SprintDto getSprintBacklog(Long boardId) {
        return sprintConverter.convertToDto
                (sprintRepository.getByBoardIdAndSprintType(boardId, SprintType.BACKLOG));
    }

    @Override
    public SprintDto createSprint(SprintDto sprintDto, Long boardId) {
        sprintDto.setBoardId(boardId);
        sprintDto.setSprintType(SprintType.SPRINT);
        sprintDto.setSprintStatus(SprintStatus.CREATED);
        Long maxSequenceValue = sprintRepository.getMaxSequenceValue(boardId);
        sprintDto.setSequenceNumber(Math.toIntExact((maxSequenceValue == null) ? 0 : ++maxSequenceValue));
        return sprintConverter.convertToDto(sprintRepository.save(
                sprintConverter.convertToEntity(sprintDto)));
    }

    @Override
    public SprintDto createSprintBacklog(Long boardId){
        SprintDto backlogDto = new SprintDto();
        backlogDto.setBoardId(boardId);
        backlogDto.setLabel("Backlog");
        backlogDto.setSprintType(SprintType.BACKLOG);
        backlogDto.setSprintStatus(SprintStatus.CREATED);
        backlogDto.setSequenceNumber(null);
        return sprintConverter.convertToDto(sprintRepository.save(
                sprintConverter.convertToEntity(backlogDto)));
    }

    @Override
    public List<SprintDto> getAllSprintsByBoardIdNotInArchive(Long boardId) {
        return sprintConverter.convertToDto(sprintRepository.getAllByBoardIdAndSprintStatusNotAndSprintType
                (boardId, SprintStatus.IN_ARCHIVE, SprintType.SPRINT));
    }

    @Override
    public SprintDto updateSprint(Long sprintId, SprintDto updatedSprintDto) {
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint", "id", sprintId));
        currentSprint.setSprintType(SprintType.SPRINT);
        Sprint updatedSprint = sprintConverter.convertToEntity(updatedSprintDto);
        currentSprint.setLabel(updatedSprint.getLabel());
        currentSprint.setStartDate(updatedSprint.getStartDate());
        currentSprint.setEndDate(updatedSprint.getEndDate());
        currentSprint.setGoal(updatedSprint.getGoal());
        currentSprint.setBoard(updatedSprint.getBoard());
        currentSprint.setSprintStatus(updatedSprint.getSprintStatus());
        currentSprint.setSequenceNumber(updatedSprint.getSequenceNumber());
        currentSprint.setDateOfEndDate(updatedSprint.getDateOfEndDate());
        return sprintConverter.convertToDto(sprintRepository.save(currentSprint));
    }

    @Override
    public void deleteSprint(Long sprintId) {
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint", "id", sprintId));
        sprintRepository.delete(currentSprint);
    }

    /*
    methods for archive function and recovery
    */
    @Override
    public SprintDto archiveSprint(Long sprintId){
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint", "id", sprintId));
        decrementNextSprint(currentSprint.getBoard().getId(), sprintId);
        currentSprint.setSequenceNumber(null);
        currentSprint.setSprintStatus(SprintStatus.IN_ARCHIVE);
        ticketService.archiveTicketsBySprintId(sprintId);
        return sprintConverter.convertToDto(sprintRepository.save(currentSprint));

    }

    @Override
    public SprintDto recoverSprint(Long sprintId){
        Sprint currentSprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint", "id", sprintId));
        currentSprint.setSprintStatus(SprintStatus.CREATED);
        ticketService.recoverTicketsBySprintId(sprintId);
        return sprintConverter.convertToDto(sprintRepository.save(currentSprint));
    }

    @Override
    public void archiveAllSprintsByBoard(Long boardId){
        List<Sprint> sprintList = sprintRepository.getAllByBoardIdAndSprintStatusNot
                (boardId, SprintStatus.IN_ARCHIVE);
        for(Sprint sprint: sprintList){
            sprint.setSprintStatus(SprintStatus.IN_ARCHIVE);
            sprintRepository.save(sprint);
        }
    }

    @Override
    public  List<SprintDto> recoverAllSprintsByBoard(Long boardId){
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
                .orElseThrow(() -> new ResourceNotFoundException("Sprint", "id", sprintId));
        List<Sprint> sprints = sprintRepository
                .findByBoardIdAndSequenceNumberGreaterThan(boardId, sprint.getSequenceNumber());
        for (Sprint s: sprints) {
            s.setSequenceNumber(s.getSequenceNumber() - 1);
        }
    }

    @Transactional
    public void updateSprintSequenceNumber(OrderSprintDto orderSprintDto) {
        Sprint sprint = sprintRepository.findById(orderSprintDto.getSprintId())
                .orElseThrow(() -> new ResourceNotFoundException("Sprint", "id", orderSprintDto.getSprintId()));
        if(sprint.getSequenceNumber() < orderSprintDto.getSequenceNumber()) {
            sprintRepository.decrementSprints(sprint.getSequenceNumber() + 1, orderSprintDto.getSequenceNumber());
            sprint.setSequenceNumber(orderSprintDto.getSequenceNumber());
            sprintRepository.save(sprint);
        } else if(sprint.getSequenceNumber() > orderSprintDto.getSequenceNumber()) {
            sprintRepository.incrementSprints(orderSprintDto.getSequenceNumber(), sprint.getSequenceNumber() - 1);
            sprint.setSequenceNumber(orderSprintDto.getSequenceNumber());
            sprintRepository.save(sprint);
        }
    }

    /*
    * Comparators for sorting by SequenceNumber
    */

    private Comparator<SprintDto> compareBySequenceNumber() {
        return (sprintDto, t1) -> (sprintDto.getSequenceNumber() - t1.getSequenceNumber());
    }

    public List<SprintDto> sortSprintsBySequenceNumber(List<SprintDto> sprintDtos) {
        if(sprintDtos!=null){
        sprintDtos.sort(compareBySequenceNumber());
        }
        return sprintDtos;
    }
}