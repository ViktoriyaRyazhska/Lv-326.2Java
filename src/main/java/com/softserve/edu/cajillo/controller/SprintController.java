package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/sprints")
public class SprintController {

    @Autowired
    SprintService sprintService;

    @PostMapping("/{boardId}")
    public void createSprint(@PathVariable("boardId") Long boardId, @RequestBody SprintDto sprintDto) {
        sprintService.createSprint(sprintDto, boardId);
    }

    @GetMapping("/{sprintId}")
    public SprintDto getSprint(@PathVariable("sprintId") Long sprintId) {
        return sprintService.getSprint(sprintId);
    }

    @PutMapping("/{sprintId}")
    public void updateSprint(@PathVariable("sprintId") Long sprintId, @RequestBody SprintDto sprintDto) {
        sprintService.updateSprint(sprintId, sprintDto);
    }

    @PutMapping("/archive/{sprintId}")
    public void archiveSprint(@PathVariable("sprintId") Long sprintId) {
        sprintService.archiveSprint(sprintId);
    }

    @PutMapping("/recover/{sprintId}")
    public SprintDto recoverSprint(@PathVariable("sprintId") Long sprintId) {
        return sprintService.recoverSprint(sprintId);
    }

    @DeleteMapping("/{sprintId}")
    public void deleteSprint(@PathVariable("sprintId") Long sprintId) {
        sprintService.deleteSprint(sprintId);
    }
}