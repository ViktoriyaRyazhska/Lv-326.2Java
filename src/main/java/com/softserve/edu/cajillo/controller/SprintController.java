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

    @PostMapping
    public void createSprint(@RequestBody SprintDto sprintDto) {
        sprintService.createSprint(sprintDto);
    }

    @GetMapping("/{sprintId}")
    public SprintDto getSprint(@PathVariable("sprintId") Long sprintId) {
        return sprintService.getSprint(sprintId);
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
    public void moveToArchiveSprint(@PathVariable("sprintId") Long sprintId) {
        sprintService.archiveSprint(sprintId);
    }

    @PostMapping("/{sprintId}")
    public SprintDto recoverSprint(@PathVariable("sprintId") Long sprintId) {
        return sprintService.recoverSprint(sprintId);
    }
}