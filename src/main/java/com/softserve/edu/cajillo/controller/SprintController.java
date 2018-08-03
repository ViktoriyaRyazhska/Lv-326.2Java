package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.entity.Sprint;
import com.softserve.edu.cajillo.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprints")
public class SprintController {

    @Autowired
    SprintService sprintService;

    @GetMapping("/{id}")
    public Sprint getSprint(@PathVariable("id") Long sprintId) {
        return sprintService.getSprint(sprintId);
    }

    @PostMapping("/sprint")
    public void createSprint(@RequestBody SprintDto sprintDto) {
        sprintService.createSprint(sprintDto);
    }

    @PostMapping("/backlog")
    public void createSprintBacklog(@RequestBody SprintDto sprintDto) {
        sprintService.createSprintBacklog(sprintDto);
    }

    @DeleteMapping("/{sprintId}")
    public void deleteSprint(@PathVariable("sprintId") Long sprintId) {
        sprintService.deleteSprint(sprintId);
    }

    @PutMapping("/{sprintId}")
    public void updateSprint(@PathVariable("sprintId") Long sprintId, @RequestBody SprintDto sprintDto) {
        sprintService.updateSprint(sprintId, sprintDto);
    }

    @PutMapping("/archive/{sprintId}")
    public void archiveSprint(@PathVariable("sprintId") Long sprintId) {
        sprintService.archiveSprint(sprintId);
    }

    @GetMapping("/board/{boardId}")
    public List<Sprint> getAllSprintsByBoard(@PathVariable("boardId") Long boardId) {
        return sprintService.getAllSprintsByBoardId(boardId);
    }

}
