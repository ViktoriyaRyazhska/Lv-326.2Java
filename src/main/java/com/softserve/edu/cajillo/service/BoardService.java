package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.entity.Board;

public interface BoardService {

    BoardDto createBoard(Board board);

    BoardDto updateBoard(Long id, Board board);

    BoardDto getBoard(Long id);

    void deleteBoard(Long id);

    BoardDto recoverBoard(Long boardId);
}