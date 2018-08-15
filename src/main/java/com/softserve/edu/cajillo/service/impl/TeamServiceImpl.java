package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.BoardConverter;
import com.softserve.edu.cajillo.converter.RelationConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.RelationDto;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Relation;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import com.softserve.edu.cajillo.exception.RelationServiceException;
import com.softserve.edu.cajillo.exception.TeamNotFoundException;
import com.softserve.edu.cajillo.repository.RelationRepository;
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
    private RelationRepository relationRepository;

    @Autowired
    private RelationConverter relationConverter;

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
        relationRepository.save(relationConverter.convertToEntity(
                new RelationDto(
                        null,
                        currentUser.getId(),
                        RoleName.ADMIN,
                        teamRepository.save(teamConverter.convertToEntity(teamDto)).getId()
                ))
        );
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
        List<Relation> allByTeamId = relationRepository.findAllByTeamId(id);
        for (Relation manager : allByTeamId) {
            if (manager.getRoleName().equals(RoleName.ADMIN)) {
                manager.setTeam(null);
                relationRepository.save(manager);
            } else {
                relationRepository.deleteById(manager.getId());
            }
        }
        teamRepository.deleteById(id);
    }

    @Override
    public void addUserToTeam(UserDto userDto, Long teamId) {
        List<Board> allBoardsForCurrentTeam = boardConverter.convertToEntity(boardService.getAllBoardsByTeamId(teamId));
        if (allBoardsForCurrentTeam != null) {
            for (Board board : allBoardsForCurrentTeam) {
                Long boardId = null;
                if (board != null) {
                    boardId= boardConverter.convertToEntity(boardService.getBoard(board.getId())).getId();
                }
                relationRepository.save(relationConverter.convertToEntity(
                        new RelationDto(
                                boardId,
                                userService.getUserByEmail(userDto.getEmail()).getId(),
                                RoleName.USER,
                                teamId)
                ));
            }
        }
    }

    @Override
    public void deleteUserFromTeam(Long userId, Long teamId) {
        List<Relation> allByTeamId = relationRepository.findAllByTeamId(teamId);
        for (Relation manager : allByTeamId) {
            List<Relation> managersToDelete = new ArrayList<>();
            if (manager.getUser().getId().equals(userId)) {
                if (manager.getRoleName().equals(RoleName.USER)) {
                    managersToDelete.add(manager);
                } else throw new RelationServiceException(CAN_NOT_DELETE);
            }
            relationRepository.deleteAll(managersToDelete);
        }
    }
}