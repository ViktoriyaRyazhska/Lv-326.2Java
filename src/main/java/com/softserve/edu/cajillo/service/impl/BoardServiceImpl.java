package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.impl.BoardConverterImpl;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.RoleManager;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.exception.UnsatisfiedException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.RoleManagerRepository;
import com.softserve.edu.cajillo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardConverterImpl boardConverter;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private RoleManagerRepository roleManagerRepository;

    public BoardDto createBoard(Board board) {
        board.setStatus(ItemsStatus.OPENED);
        Board save = boardRepository.save(board);
        return boardConverter.convertToDto(save);
    }

    public BoardDto updateBoard(Long id, Board board) {
        Board existedBoard = boardRepository.findById(id)
                .orElseThrow(() -> new UnsatisfiedException(String.format("Board with id %d not found", id)));
        existedBoard.setName(board.getName());
        return boardConverter.convertToDto(boardRepository.save(existedBoard));
    }

    public BoardDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new UnsatisfiedException(String.format("Board with id %d not found", id)));
        return boardConverter.convertToDto(board);
    }

    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new UnsatisfiedException(String.format("Board with id %d not found", id)));
        board.setStatus(ItemsStatus.DELETED);
        boardRepository.save(board);
    }

    public List<Board> getAllBoardsByTeamId(Long teamId){
        List<Board> allBoardsForCurrentTeam = new ArrayList<>();
        List<RoleManager> allManagers = roleManagerRepository.findAllByTeamId(teamId);
        if (allManagers != null){
            for (RoleManager manager : allManagers) {
                allBoardsForCurrentTeam.add(manager.getBoard());
            }
        }
        return allBoardsForCurrentTeam;
    }
}