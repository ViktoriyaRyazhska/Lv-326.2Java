package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping
    public BoardDto createBoard(@RequestBody Board board, @CurrentUser UserPrincipal userPrincipal) {
        return boardService.createBoard(board, userPrincipal);
    }

    @PutMapping("/{id}")
    public BoardDto updateBoard(@PathVariable Long id, @RequestBody Board board) {
        return boardService.updateBoard(id, board);
    }

    @GetMapping("/{id}")
    public BoardDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @GetMapping
    public List<BoardDto> getAllUserBoards(@CurrentUser UserPrincipal currentUser){
        return boardService.getAllUserBoards(currentUser);
    }

    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }

    @PostMapping("/{boardId}")
    public BoardDto recoverBoard(@PathVariable("boardId") Long boardId) {
        return boardService.recoverBoard(boardId);
    }

    @PutMapping("/image")
    public void setBoardBackground(@RequestBody BoardDto boardDto) {
        boardService.saveBoardBackground(boardDto);
    }

    @PostMapping("/{boardId}/user")
    public void addUserToBoard(@PathVariable Long boardId,
                               @RequestBody UserDto userDto,
                               @CurrentUser UserPrincipal userPrincipal){
        boardService.addUserToBoard(boardId, userDto, userPrincipal);
    }

    @DeleteMapping("/{boardId}/{userId}")
    public void deleteUserFromBoard(@PathVariable Long boardId, @PathVariable Long userId){
        boardService.deleteUserFromBoard(boardId, userId);
    }

    @GetMapping("/images/{boardId}")
    public List<String> getAllBackgroundImagesByBoardId(@PathVariable Long boardId) {
        return boardService.getAllBackgroundImagesByBoardId(boardId);
    }

    @PutMapping("/images/{boardId}")
    public void setExistingImageOnBackground(@PathVariable Long boardId, @RequestBody String imageUrl) {
        boardService.setExistingImageOnBackground(boardId, imageUrl);
    }

    @DeleteMapping("/images/{boardId}")
    public void clearBoardBackground(@PathVariable Long boardId) {
        boardService.clearBoardBackground(boardId);
    }
}