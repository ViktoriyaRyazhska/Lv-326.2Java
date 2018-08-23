package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.security.UserPrincipal;

import java.util.List;

public interface BoardService {

    BoardDto createBoard(Board board, UserPrincipal userPrincipal);

    BoardDto updateBoard(Long id, Board board);

    BoardDto getBoard(Long id);

    List<BoardDto> getAllUserBoards(UserPrincipal currentUser);

    void deleteBoard(Long id);

    List<BoardDto> getAllActiveBoardsByTeamId(Long teamId);

    BoardDto createNewTeamBoard(Long teamId, Board board);

    void addBoardToTeam(Long teamId, Long boardId);

    void deleteTeamBoard(Long teamId, Long boardId);
  
    BoardDto recoverBoard(Long boardId);

    Board getBoardEntity(Long id);

    void saveBoardBackground(BoardDto boardDto);

    void addUserToBoard(Long boardId, UserDto userDto, UserPrincipal userPrincipal);

    void deleteUserFromBoard(Long boardId, Long userId);
}