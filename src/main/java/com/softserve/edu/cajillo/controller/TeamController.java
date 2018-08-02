package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    TeamConverter teamConverter;

    @GetMapping("/{id}")
    public Team getTeam(@PathVariable Long id) {
        return teamService.getTeam(id);
    }

    @PostMapping("/createTeam")
    public void createTeam(@RequestBody TeamDto teamDto, @CurrentUser UserPrincipal currentUser) {
        teamService.createTeam(teamDto, currentUser);
    }
}