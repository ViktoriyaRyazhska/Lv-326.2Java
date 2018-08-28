package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.BoardConverter;
import com.softserve.edu.cajillo.converter.RelationConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Relation;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import com.softserve.edu.cajillo.exception.FileOperationException;
import com.softserve.edu.cajillo.exception.RelationServiceException;
import com.softserve.edu.cajillo.exception.ResourceNotFoundException;
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
import java.util.*;

@Slf4j
@Service
public class TeamServiceImpl implements TeamService {

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
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));
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
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));
        if (team.getName() != null) {
            existedTeam.setName(team.getName());
        }
        if (team.getDescription() != null) {
            existedTeam.setDescription(team.getDescription());
        }
        if (team.getAvatar() != null) {
            existedTeam.setAvatar(team.getAvatar());
        }
        return teamConverter.convertToDto(teamRepository.save(existedTeam));
    }

    private List<Relation> saveForAdminAndDeleteForOthers(Long teamId) {
        List<Relation> allByTeamId = relationRepository.findAllByTeamId(teamId);
        for (Relation relation : allByTeamId) {
            if (relation.getRoleName().equals(RoleName.ADMIN)) {
                relation.setTeam(null);
                if (relation.getBoard() != null) {
                    boardService.deleteBoard(relation.getBoard().getId());
                }
                relationRepository.save(relation);
            } else {
                relationRepository.deleteById(relation.getId());
            }
        }
        return allByTeamId;
    }

    @Override
    public void deleteTeam(Long teamId) {
        saveForAdminAndDeleteForOthers(teamId);
        teamRepository.deleteById(teamId);
    }

    private void saveRelation(Long boardId, Long userId, Long teamId) {
        relationRepository.save(relationConverter.convertToEntity(
                new RelationDto(
                        boardId,
                        userId,
                        RoleName.USER,
                        teamId)
        ));
    }

    @Override
    public void addUserToTeam(UserDto userDto, Long teamId) {
       if(getTeam(teamId) != null) {
           User newTeamMember = userService.getUserByEmail(userDto.getEmail());
           List<Board> allBoardsForCurrentTeam = boardConverter.convertToEntity(boardService.getAllActiveBoardsByTeamId(teamId));
           Long boardId = null;
           List<Relation> allByTeamId = relationRepository.findAllByTeamId(teamId);
           List<Relation> allByUserId = relationRepository.findAllByUserId(newTeamMember.getId());
           if (allBoardsForCurrentTeam.size() != 0) {
               if (allByUserId.size() == 0 || !allByUserId.containsAll(allByTeamId)) {
                   for (Board board : allBoardsForCurrentTeam) {
                       boardId = board.getId();
                       saveRelation(boardId, newTeamMember.getId(), teamId);
                   }
               }
           } else {
               if (allByUserId.size() == 0) {
                   saveRelation(boardId, newTeamMember.getId(), teamId);
               } else if (!allByTeamId.containsAll(allByUserId)) {
                   saveRelation(boardId, newTeamMember.getId(), teamId);
               }
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
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));
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
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId)).getAvatar());
    }

    @Override
    public void deleteTeamAvatar(Long teamId) {
        log.info("Deleting avatar for user with id: " + teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team", "id", teamId));
        team.setAvatar(null);
        teamRepository.save(team);
    }
}