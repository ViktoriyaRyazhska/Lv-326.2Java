package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.impl.BoardConverterImpl;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.SprintDto;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.RoleManager;
import com.softserve.edu.cajillo.entity.enums.BoardType;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.entity.enums.SprintStatus;
import com.softserve.edu.cajillo.entity.enums.SprintType;
import com.softserve.edu.cajillo.exception.UnsatisfiedException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.RoleManagerRepository;
import com.softserve.edu.cajillo.service.BoardService;
import com.softserve.edu.cajillo.service.SprintService;
import com.softserve.edu.cajillo.service.TableListService;
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
  
    @Autowired
    private TableListService tableListService;

    @Autowired
    private SprintService sprintService;

    public BoardDto createBoard(Board board) {
        board.setStatus(ItemsStatus.OPENED);
        Board save = boardRepository.save(board);
        if (board.getBoardType() == BoardType.SCRUM) {
            SprintDto sprintDto = generateBacklog(save);
            sprintService.createSprintBacklog(sprintDto);
        }
        return boardConverter.convertToDto(save);
    }

    public SprintDto generateBacklog(Board board) {
        SprintDto sprintDto = new SprintDto();
        sprintDto.setLabel("Backlog");
        sprintDto.setBoardId(board.getId());
        return sprintDto;
    }

    public BoardDto updateBoard(Long id, Board board) {
        Board existedBoard = boardRepository.findById(id)
                .orElseThrow(() -> new UnsatisfiedException(String.format("Board with id %d not found", id)));
        existedBoard.setName(board.getName());
        return boardConverter.convertToDto(boardRepository.save(existedBoard));
    }

    public BoardDto getBoard(Long id) {
        Board board = boardRepository.findByIdAndStatus(id, ItemsStatus.OPENED);
        if (board == null)
            throw new UnsatisfiedException(String.format("Board with id %d not found", id));
        return boardConverter.convertToDto(board);
    }

    public Board getBoardEntity(Long id) {
        Board board = boardRepository.findByIdAndStatus(id, ItemsStatus.OPENED);
        if (board == null)
            throw new UnsatisfiedException(String.format("Board with id %d not found", id));
        return board;
    }

    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new UnsatisfiedException(String.format("Board with id %d not found", id)));
        board.setStatus(ItemsStatus.DELETED);
        boardRepository.save(board);
        deleteAllInternalBoardItems(id);
    }

    private void deleteAllInternalBoardItems(Long boardId) {
        tableListService.deleteTableListsByBoardId(boardId);
    }

    public BoardDto recoverBoard(Long boardId) {
        Board board = boardRepository.findByIdAndStatus(boardId, ItemsStatus.DELETED);
        board.setStatus(ItemsStatus.OPENED);
        recoverAllInternalItems(boardId);
        boardRepository.save(board);
        return boardConverter.convertToDto(board);
    }

    private void recoverAllInternalItems(Long boardId) {
        tableListService.recoverTableListsByBoardId(boardId);
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