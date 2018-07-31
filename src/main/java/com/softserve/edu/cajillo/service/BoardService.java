package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.entity.Board;

public interface BoardService {

    public BoardDto createBoard(Board board);

    public BoardDto updateBoard(Long id, Board board);

    public BoardDto getBoard(Long id);

    public void deleteBoard(Long id);
}