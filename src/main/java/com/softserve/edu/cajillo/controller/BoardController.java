package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping("/api/boards")
    public BoardDto createBoard(@RequestBody Board board) {
        return boardService.createBoard(board);
    }

    @PutMapping("/api/boards/{id}")
    public BoardDto updateBoard(@PathVariable Long id, @RequestBody Board board) {
        return boardService.updateBoard(id, board);
    }

    @GetMapping("/api/boards/{id}")
    public BoardDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @DeleteMapping("/api/boards/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }
}