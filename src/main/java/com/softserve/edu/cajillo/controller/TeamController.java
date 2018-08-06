package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.RoleManagerService;
import com.softserve.edu.cajillo.service.TeamService;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @Autowired
    TeamConverter teamConverter;

    @Autowired
    RoleManagerService roleManagerService;

    @GetMapping("/{id}")
    public TeamDto getTeam(@PathVariable Long id) {
        return teamService.getTeam(id);
    }

    @PostMapping("/createTeam")
    @ResponseStatus(HttpStatus.OK)
    public void createTeam(@RequestBody TeamDto teamDto, @CurrentUser UserPrincipal currentUser) {
        teamService.createTeam(teamDto, currentUser);
    }

    @PutMapping("/updateTeam/{id}")
    public TeamDto updateTeam(@PathVariable Long id, @RequestBody Team team) {
        return teamService.updateTeam(id, team);
    }

    @DeleteMapping("/deleteTeam/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }

    @PostMapping("/{teamId}/addUser")
    @ResponseStatus(HttpStatus.OK)
    public void addUserToTeam(@RequestBody UserDto userDto, @PathVariable Long teamId) {
        teamService.addUserToTeam(userDto, teamId);
    }

    @DeleteMapping("{teamId}/{userId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        teamService.deleteUserFromTeam(userId, teamId);
    }
}