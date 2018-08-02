package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.security.UserPrincipal;

public interface TeamService {

    Team getTeam(Long id);

    void createTeam(TeamDto teamDto, UserPrincipal currentUser);
}