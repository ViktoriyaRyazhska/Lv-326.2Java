package com.softserve.edu.cajillo.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.softserve.edu.cajillo.converter.impl.BoardConverterImpl;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.RoleManager;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.enums.BoardType;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.exception.BoardNotFoundException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.RoleManagerRepository;
import com.softserve.edu.cajillo.service.BoardService;
import com.softserve.edu.cajillo.service.SprintService;
import com.softserve.edu.cajillo.service.TableListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class BoardServiceImpl implements BoardService {

    @Value("${CLOUDINARYURL}")
    private String cloudUrl;

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
            sprintService.createSprintBacklog(board.getId());
            TableList tableList = new TableList();
            tableList.setName("To Do");
            tableList.setSequenceNumber(0);
            tableListService.createTableList(board.getId(), tableList);
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
        BoardDto boardDto = boardConverter.convertToDto(board);
        sortTableListsBySequenceNumber(boardDto);
        sprintService.sortSprintsBySequenceNumber(boardDto);
        return boardDto;
    }

    public Board getBoardEntity(Long id) {
        Board board = boardRepository.findByIdAndStatus(id, ItemsStatus.OPENED)
                .orElseThrow(() -> new BoardNotFoundException(String.format("Board with id %d not found", id)));
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

    public void saveBoardBackground(BoardDto boardDto){
        byte[] decodedImg = Base64.getDecoder().decode(boardDto.getImage().getBytes(StandardCharsets.UTF_8));
        String cloudImageUrl = uploadImageOnCloud(decodedImg, boardDto);
        setCurrentImageUrlToBoard(cloudImageUrl, boardDto.getId());
    }

    private void setCurrentImageUrlToBoard(String cloudImageUrl, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(String.format("Board with id %d not found", boardId)));
        board.setImage(cloudImageUrl);
        boardRepository.save(board);
    }

    private String uploadImageOnCloud(byte[] imageFile, BoardDto boardDto) {
        try {
            Cloudinary cloudinary = new Cloudinary(cloudUrl);
            Map params = ObjectUtils.asMap("public_id", "board_images/"
                    + boardDto.getId() + "/" + boardDto.getImageName());
            Map uploadResult = cloudinary.uploader().upload(imageFile, params);
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sortTableListsBySequenceNumber(BoardDto boardDto) {
        List<TableListDto> tableListDtos = boardDto.getTableLists();
        quickSort(0, tableListDtos.size() - 1, tableListDtos);
    }

    private void quickSort(int lowerIndex, int higherIndex, List<TableListDto> tableListDtos) {
        int i = lowerIndex;
        int j = higherIndex;
        TableListDto pivot = tableListDtos.get(lowerIndex+(higherIndex-lowerIndex)/2);
        while (i <= j) {
            while (tableListDtos.get(i).getSequenceNumber() < pivot.getSequenceNumber()) {
                i++;
            }
            while (tableListDtos.get(j).getSequenceNumber() > pivot.getSequenceNumber()) {
                j--;
            }
            if (i <= j) {
                Collections.swap(tableListDtos, i, j);
                i++;
                j--;
            }
        }
        if (lowerIndex < j)
            quickSort(lowerIndex, j, tableListDtos);
        if (i < higherIndex)
            quickSort(i, higherIndex, tableListDtos);
    }
}

