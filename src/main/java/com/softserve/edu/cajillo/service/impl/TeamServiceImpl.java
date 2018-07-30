package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.dto.TeamDTO;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.exception.TeamNotFoundException;
import com.softserve.edu.cajillo.repository.TeamRepository;
import com.softserve.edu.cajillo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {

    private static final String TEAM_ID_NOT_FOUND_MESSAGE = "Could not find team with id=";

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Team getTeam(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new TeamNotFoundException(TEAM_ID_NOT_FOUND_MESSAGE + id));
        return team;
    }

    @Override
    public Team createTeam(TeamDTO createTeamDTO) {
        Team team = new Team();
        team.setName(createTeamDTO.getName());
        team.setDescription(createTeamDTO.getDescription());
        return team;
    }
}
