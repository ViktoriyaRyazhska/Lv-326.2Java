package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.AvatarDto;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Relation;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeamService {

    TeamDto getTeam(Long id);

    void createTeam(TeamDto teamDto, UserPrincipal currentUser);

    TeamDto updateTeam(Long id, Team team);

    void deleteTeam(Long id);

    void addUserToTeam(UserDto userDto, Long teamId);

    void deleteUserFromTeam(Long userId, Long teamId);

    void uploadAvatar(Long teamId, MultipartFile avatar);

    AvatarDto getTeamAvatar(Long teamId);

    void deleteTeamAvatar(Long teamId);
}