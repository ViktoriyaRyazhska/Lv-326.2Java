package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.AvatarDto;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.TeamDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Team;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.BoardService;
import com.softserve.edu.cajillo.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private BoardService boardService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createTeam(@RequestBody TeamDto teamDto, @CurrentUser UserPrincipal currentUser) {
        teamService.createTeam(teamDto, currentUser);
    }

    @GetMapping("/{teamId}")
    public TeamDto getTeam(@PathVariable Long teamId) {
        return teamService.getTeam(teamId);
    }

    @PutMapping("/{teamId}")
    public TeamDto updateTeam(@PathVariable Long teamId, @RequestBody Team team) {
        return teamService.updateTeam(teamId, team);
    }

    @DeleteMapping("/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
    }

    @PostMapping("/{teamId}/avatar")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadTeamAvatar(@PathVariable Long teamId, @RequestParam("file") MultipartFile avatar) {
        teamService.uploadAvatar(teamId, avatar);
    }

    @DeleteMapping("/{teamId}/avatar")
    @PreAuthorize("hasRole('ACTIVE')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTeamAvatar(@PathVariable Long teamId) {
        teamService.deleteTeamAvatar(teamId);
    }

    @GetMapping("/{teamId}/avatar")
    public AvatarDto getTeamAvatar(@PathVariable Long teamId) {
        return teamService.getTeamAvatar(teamId);
    }

    @PostMapping("/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public void addUserToTeam(@RequestBody UserDto userDto, @PathVariable Long teamId) {
        teamService.addUserToTeam(userDto, teamId);
    }

    @DeleteMapping("{teamId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        teamService.deleteUserFromTeam(userId, teamId);
    }

    @GetMapping("/{teamId}/boards")
    public List<BoardDto> getAllTeamBoards(@PathVariable Long teamId) {
        return boardService.getAllBoardsByTeamId(teamId);
    }

    @PostMapping("/{teamId}/boards")
    public BoardDto createNewTeamBoard(@PathVariable Long teamId, @RequestBody Board board) {
        return boardService.createNewTeamBoard(teamId, board);
    }
//
//    @DeleteMapping{"/{teamId}/boards/{boardId}"}
//    public void deleteTeamBoard(@PathVariable ("boardId") Long boardId){
//        boardService.deleteTeamBoard(boardId);
//    }

//    @PutMapping("/{teamId}/board")
//    public Board addBoardToTeam (@PathVariable Long id, ){}
}