package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.security.UserPrincipal;

public interface TeamService {

    TeamDto getTeam(Long id);

    void createTeam(TeamDto teamDto, UserPrincipal currentUser);

    TeamDto updateTeam(Long id, Team team);

    void addUserToTeam(UserDto userDto, Long teamId);
}