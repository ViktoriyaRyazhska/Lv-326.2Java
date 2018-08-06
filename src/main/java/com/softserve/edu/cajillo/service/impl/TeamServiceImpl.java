package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.RoleManagerConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.RoleManagerDto;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import com.softserve.edu.cajillo.exception.TeamNotFoundException;
import com.softserve.edu.cajillo.repository.RoleManagerRepository;
import com.softserve.edu.cajillo.repository.TeamRepository;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.BoardService;
import com.softserve.edu.cajillo.service.TeamService;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Override
    public TeamDto getTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_ID_NOT_FOUND_MESSAGE + id));
        return teamConverter.convertToDto(team);
    }

    @Override
    public void createTeam(TeamDto teamDto, UserPrincipal currentUser) {
        Team saveTeam = teamRepository.save(teamConverter.convertToEntity(teamDto));
        roleManagerRepository.save(roleManagerConverter.convertToEntity(
                new RoleManagerDto(
                        0L, //TODO default value
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

    @Override
    public void addUserToTeam(UserDto userDto, Long teamId) {
        User newTeamMember = userService.getUserByEmail(userDto.getEmail());
        List<Board> allBoardsForCurrentTeam = boardService.getAllBoardsByTeamId(teamId);
        if (allBoardsForCurrentTeam != null) {
            for (Board board : allBoardsForCurrentTeam) {
                roleManagerRepository.save(roleManagerConverter.convertToEntity(
                        new RoleManagerDto(
                                board.getId(),
                                newTeamMember.getId(),
                                RoleName.USER,
                                teamId
                        )
                ));
            }
        }
    }
}