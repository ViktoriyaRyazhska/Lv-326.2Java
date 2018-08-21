package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.OrderSprintDto;
import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class SprintController {

    @Autowired
    private SprintService sprintService;

    @PostMapping("/sprint/{boardId}")
    public SprintDto createSprint(@PathVariable("boardId") Long boardId, @RequestBody SprintDto sprintDto) {
        return sprintService.createSprint(sprintDto, boardId);
    }

    @GetMapping("/sprint/backlog/{boardId}")
    public SprintDto getSprintBacklog(@PathVariable("boardId") Long boardId) {
        return sprintService.getSprintBacklog(boardId);
    }

    @GetMapping("/sprint/{sprintId}")
    public SprintDto getSprint(@PathVariable("sprintId") Long sprintId) {
        return sprintService.getSprint(sprintId);
    }

    @PutMapping("/sprint/{sprintId}")
    public SprintDto updateSprint(@PathVariable("sprintId") Long sprintId, @RequestBody SprintDto sprintDto) {
        return sprintService.updateSprint(sprintId, sprintDto);
    }

    @DeleteMapping("/sprint/archive/{sprintId}")
    public void archiveSprint(@PathVariable("sprintId") Long sprintId) {
        sprintService.archiveSprint(sprintId);
    }

    @PutMapping("sprint/recover/{sprintId}")
    public SprintDto recoverSprint(@PathVariable("sprintId") Long sprintId) {
        return sprintService.recoverSprint(sprintId);
    }

    @DeleteMapping("sprint/{sprintId}")
    public void deleteSprint(@PathVariable("sprintId") Long sprintId) {
        sprintService.deleteSprint(sprintId);
    }

    @PutMapping("/sprint/order")
    public void updateSprintSequenceNumber(@RequestBody OrderSprintDto orderSprintDto) {
        sprintService.updateSprintSequenceNumber(orderSprintDto);
    }
}