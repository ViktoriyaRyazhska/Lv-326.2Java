package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.RoleManagerConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.RoleManagerDto;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import com.softserve.edu.cajillo.exception.TeamNotFoundException;
import com.softserve.edu.cajillo.repository.RoleManagerRepository;
import com.softserve.edu.cajillo.repository.TeamRepository;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {

    private static final String TEAM_ID_NOT_FOUND_MESSAGE = "Could not find team with id=";

    @Autowired
    private TeamConverter teamConverter;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RoleManagerRepository roleManagerRepository;

    @Autowired
    private RoleManagerConverter roleManagerConverter;

    @Override
    public Team getTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_ID_NOT_FOUND_MESSAGE + id));
        return team;
//        return teamConverter.convertToDto(team);
    }

    @Override
    public void createTeam(TeamDto teamDto, UserPrincipal currentUser) {
        Team saveTeam = teamRepository.save(teamConverter.convertToEntity(teamDto));
        roleManagerRepository.save(roleManagerConverter.convertToEntity(
                new RoleManagerDto(
                        0L,
                        currentUser.getId(),
                        RoleName.ADMIN,
                        saveTeam.getId()
                )
        ));
    }

    @Override
    public TeamDto updateTeam(Long id, Team team) {
        Team existedTeam = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_ID_NOT_FOUND_MESSAGE + id));
        existedTeam.setName(team.getName());
        existedTeam.setDescription(team.getDescription());
        existedTeam.setAvatar(team.getAvatar());
        return teamConverter.convertToDto(teamRepository.save(existedTeam));
    }
}