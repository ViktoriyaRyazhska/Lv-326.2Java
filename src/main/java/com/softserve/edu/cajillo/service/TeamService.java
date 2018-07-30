package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.TeamDTO;
import com.softserve.edu.cajillo.entity.Team;

public interface TeamService {

    Team getTeam(Long id);

    Team createTeam(TeamDTO teamDTO);

}
