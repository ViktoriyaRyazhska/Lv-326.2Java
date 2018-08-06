package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.BoardConverter;
import com.softserve.edu.cajillo.converter.RoleManagerConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.RoleManagerDto;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.RoleManager;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import com.softserve.edu.cajillo.exception.RoleManagerServiceException;
import com.softserve.edu.cajillo.exception.TeamNotFoundException;
import com.softserve.edu.cajillo.repository.RoleManagerRepository;
import com.softserve.edu.cajillo.repository.TeamRepository;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.BoardService;
import com.softserve.edu.cajillo.service.TeamService;
import com.softserve.edu.cajillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private static final String TEAM_ID_NOT_FOUND_MESSAGE = "Could not find team with id=";
    private static final String ROLE_MANAGER_NOT_FOUND_MESSAGE = "Could not find roleManager";
    private static final String CAN_NOT_DELETE = "You can't delete this user, he is admin";

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

    @Autowired
    private BoardConverter boardConverter;

    @Override
    public TeamDto getTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_ID_NOT_FOUND_MESSAGE + id));
        return teamConverter.convertToDto(team);
    }

    @Override
    public void createTeam(TeamDto teamDto, UserPrincipal currentUser) {
        RoleManager savedRoleManager = roleManagerRepository.save(roleManagerConverter.convertToEntity(
                new RoleManagerDto(
                        null,
                        currentUser.getId(),
                        RoleName.ADMIN,
                        null
                )
        ));
        Team savedTeam = teamRepository.save(teamConverter.convertToEntity(teamDto));
        savedRoleManager.setTeam(savedTeam);
        roleManagerRepository.save(savedRoleManager);
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
    public void deleteTeam(Long id) {
        List<RoleManager> allByTeamId = roleManagerRepository.findAllByTeamId(id);
        for (RoleManager manager : allByTeamId) {
            if (manager.getRoleName().equals(RoleName.ADMIN)) {
                manager.setTeam(null);
                roleManagerRepository.save(manager);
            } else {
                roleManagerRepository.deleteById(manager.getId());
            }
        }
        teamRepository.deleteById(id);
    }

    @Override
    public void addUserToTeam(UserDto userDto, Long teamId) {
        User newTeamMember = userService.getUserByEmail(userDto.getEmail());
        RoleManager savedRoleManager = roleManagerRepository.save(
                roleManagerConverter.convertToEntity(
                        new RoleManagerDto(
                                null,
                                newTeamMember.getId(),
                                RoleName.USER,
                                null)
                ));
        RoleManager roleManager = roleManagerRepository.findById(savedRoleManager.getId())
                .orElseThrow(() -> new RoleManagerServiceException(ROLE_MANAGER_NOT_FOUND_MESSAGE));
        roleManager.setTeam(teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_ID_NOT_FOUND_MESSAGE + teamId)));

        List<Board> allBoardsForCurrentTeam = boardService.getAllBoardsByTeamId(teamId);
        if (allBoardsForCurrentTeam != null) {
            for (Board board : allBoardsForCurrentTeam) {
                if (board != null) {
                    roleManager.setBoard(boardConverter.convertToEntity(boardService.getBoard(board.getId())));
                }
            }
        }
        roleManagerRepository.save(roleManager);
    }

    @Override
    public void deleteUserFromTeam(Long userId, Long teamId) {
        List<RoleManager> allByTeamId = roleManagerRepository.findAllByTeamId(teamId);
        for (RoleManager manager : allByTeamId) {
            List<RoleManager> managersToDelete = new ArrayList<>();
            if (manager.getUser().getId().equals(userId)) {
                if (manager.getRoleName().equals(RoleName.USER)) {
                    managersToDelete.add(manager);
                } else throw new RoleManagerServiceException(CAN_NOT_DELETE);
            }
            roleManagerRepository.deleteAll(managersToDelete);
        }
    }
}