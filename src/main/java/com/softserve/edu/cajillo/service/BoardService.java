package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.entity.Board;

import java.util.List;

public interface BoardService {

    BoardDto createBoard(Board board);

    BoardDto updateBoard(Long id, Board board);

    BoardDto getBoard(Long id);

    void deleteBoard(Long id);

    List<Board> getAllBoardsByTeamId(Long teamId);
  
    BoardDto recoverBoard(Long boardId);

    Board getBoardEntity(Long id);

}