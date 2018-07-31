package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    private TeamConverter teamConverter;

    @GetMapping("/{id}")
    public TeamDto getTeam(@PathVariable("id") Long id) {
        return teamConverter.convertToDto(teamService.getTeam(id));
    }
}