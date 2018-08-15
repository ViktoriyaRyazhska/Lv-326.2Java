package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.RelationConverter;
import com.softserve.edu.cajillo.converter.impl.BoardConverterImpl;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.RelationDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Relation;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.enums.BoardType;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import com.softserve.edu.cajillo.exception.BoardNotFoundException;
import com.softserve.edu.cajillo.exception.UnsatisfiedException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.RelationRepository;
import com.softserve.edu.cajillo.service.BoardService;
import com.softserve.edu.cajillo.service.RelationService;
import com.softserve.edu.cajillo.service.SprintService;
import com.softserve.edu.cajillo.service.TableListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardConverterImpl boardConverter;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private RelationService relationService;

    @Autowired
    private RelationConverter relationConverter;
    @Autowired
    private TableListService tableListService;

    @Autowired
    private SprintService sprintService;

    public BoardDto createBoard(Board board) {
        board.setStatus(ItemsStatus.OPENED);
        Board save = boardRepository.save(board);
        if (board.getBoardType() == BoardType.SCRUM) {
            sprintService.createSprintBacklog(board.getId());
        }
        return boardConverter.convertToDto(save);
    }

    public BoardDto updateBoard(Long id, Board board) {
        Board existedBoard = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException(String.format("Board with id %d not found", id)));
        existedBoard.setName(board.getName());
        return boardConverter.convertToDto(boardRepository.save(existedBoard));
    }

    public BoardDto getBoard(Long id) {
        Board board = boardRepository.findByIdAndStatus(id, ItemsStatus.OPENED)
                .orElseThrow(() -> new BoardNotFoundException(String.format("Board with id %d not found", id)));
        if (board == null)
            throw new UnsatisfiedException(String.format("Board with id %d not found", id));
        return boardConverter.convertToDto(board);
    }

    public Board getBoardEntity(Long id) {
        Board board = boardRepository.findByIdAndStatus(id, ItemsStatus.OPENED)
                .orElseThrow(() -> new BoardNotFoundException(String.format("Board with id %d not found", id)));
        if (board == null)
            throw new UnsatisfiedException(String.format("Board with id %d not found", id));
        return board;
    }

    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException(String.format("Board with id %d not found", id)));
        deleteAllInternalBoardItems(id);
        board.setStatus(ItemsStatus.DELETED);
        boardRepository.save(board);
    }

    private void deleteAllInternalBoardItems(Long boardId) {
        tableListService.deleteTableListsByBoardId(boardId);
        sprintService.archiveAllSprintsByBoard(boardId);

    }

    public BoardDto recoverBoard(Long boardId) {
        Board board = boardRepository.findByIdAndStatus(boardId, ItemsStatus.DELETED)
                .orElseThrow(() -> new BoardNotFoundException(String.format("Board with id %d not found", boardId)));
        board.setStatus(ItemsStatus.OPENED);
        recoverAllInternalItems(boardId);
        boardRepository.save(board);
        return boardConverter.convertToDto(board);
    }

    private void recoverAllInternalItems(Long boardId) {
        tableListService.recoverTableListsByBoardId(boardId);
        sprintService.recoverAllSprintsByBoard(boardId);
    }

    public List<BoardDto> getAllBoardsByTeamId(Long teamId){
        List<BoardDto> allBoardsForCurrentTeam = new ArrayList<>();
        List<Relation> allManagers = relationRepository.findAllByTeamId(teamId);
        if (allManagers != null){
            for (Relation manager : allManagers) {
                allBoardsForCurrentTeam.add(boardConverter.convertToDto(manager.getBoard()));
            }
        }
        return allBoardsForCurrentTeam;
    }

    @Override
    public BoardDto createNewTeamBoard(Long teamId, Board board) {
        Map<User, RoleName> allUsersInTeam = relationService.getAllUsersInTeam(teamId);
        board.setStatus(ItemsStatus.OPENED);
        Board savedBoard = boardRepository.save(board);

        for (Map.Entry<User, RoleName> entry : allUsersInTeam.entrySet()) {
            User user = entry.getKey();
            RoleName role = entry.getValue();

            relationRepository.save(relationConverter.convertToEntity(
                    new RelationDto(
                            savedBoard.getId(),
                            user.getId(),
                            role,
                            teamId)
            ));
        }
        if (board.getBoardType().equals(BoardType.SCRUM)) {
            sprintService.createSprintBacklog(board.getId());
        }
        return boardConverter.convertToDto(savedBoard);
    }
}