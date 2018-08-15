package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.BoardConverter;
import com.softserve.edu.cajillo.converter.RelationConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.AvatarDto;
import com.softserve.edu.cajillo.dto.RelationDto;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Relation;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import com.softserve.edu.cajillo.exception.FileOperationException;
import com.softserve.edu.cajillo.exception.RelationServiceException;
import com.softserve.edu.cajillo.exception.TeamNotFoundException;
import com.softserve.edu.cajillo.exception.UserNotFoundException;
import com.softserve.edu.cajillo.repository.RelationRepository;
import com.softserve.edu.cajillo.repository.TeamRepository;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.BoardService;
import com.softserve.edu.cajillo.service.TeamService;
import com.softserve.edu.cajillo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class TeamServiceImpl implements TeamService{

    private static final String TEAM_ID_NOT_FOUND_MESSAGE = "Could not find team with id=";
    private static final String CAN_NOT_DELETE = "You can't delete this user, he is admin";
    private static final String REQUEST_ENTITY_TOO_LARGE_ERROR_MESSAGE = "Avatar size is too large. Maximum size is 256 KiB";
    private static final String UNSUPPORTED_MIME_TYPES_ERROR_MESSAGE = "Unsupported media type";
    private static final String FILES_SAVE_ERROR_MESSAGE = "Could not save file for user with id=%s";


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

    public void uploadAvatar(Long teamId, MultipartFile avatar) {
        log.info("Uploading avatar for team with id: " + teamId);
        if (avatar.getSize() > (256 * 1024)) {
            log.error("Avatar file is to large. Size = " + avatar.getSize() + ", max size is " + (256 * 1024));
            throw new UnsupportedOperationException(REQUEST_ENTITY_TOO_LARGE_ERROR_MESSAGE);
        } else if (!Arrays.asList("image/jpeg", "image/pjpeg", "image/png").contains(avatar.getContentType())) {
            log.error(avatar.getContentType() + " is not supported");
            throw new UnsupportedOperationException(UNSUPPORTED_MIME_TYPES_ERROR_MESSAGE);
        }
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new TeamNotFoundException(TEAM_ID_NOT_FOUND_MESSAGE + teamId));
        try {
            team.setAvatar(Base64.getMimeEncoder().encodeToString(avatar.getBytes()));
            teamRepository.save(team);
        } catch (IOException e) {
            log.error(e.toString());
            throw new FileOperationException(String.format(FILES_SAVE_ERROR_MESSAGE, teamId));
        }
    }

    @Override
    public AvatarDto getTeamAvatar(Long teamId) {
        log.info("Getting avatar for team with id: " + teamId);
        return new AvatarDto(teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_ID_NOT_FOUND_MESSAGE + teamId)).getAvatar());
    }

    @Override
    public void deleteTeamAvatar(Long teamId) {
        log.info("Deleting avatar for user with id: " + teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_ID_NOT_FOUND_MESSAGE + teamId));
        team.setAvatar(null); // maybe set default avatar???
        teamRepository.save(team);
    }
}