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

    @PostMapping("create/sprint")
    public void createSprint(@RequestBody SprintDto sprintDto) {
        sprintService.createSprint(sprintDto);
    }

    @PostMapping("create/backlog")
    public void createSprintBacklog(@RequestBody SprintDto sprintDto) {
        sprintService.createSprintBacklog(sprintDto);
    }

    @DeleteMapping("/delete/{sprintId}")
    public void deleteSprint(@PathVariable("sprintId") Long sprintId) {
        sprintService.deleteSprint(sprintId);
    }

    @PutMapping("/update/{sprintId}")
    public void updateSprint(@PathVariable("sprintId") Long sprintId, @RequestBody SprintDto sprintDto) {
        sprintService.updateSprint(sprintId, sprintDto);
    }

    @GetMapping("/board/{boardId}")
    public List<Sprint> getAllSprintsByBoard(@PathVariable("boardId") Long boardId) {
        return sprintService.getAllSprintsByBoard(boardId);
    }

}
