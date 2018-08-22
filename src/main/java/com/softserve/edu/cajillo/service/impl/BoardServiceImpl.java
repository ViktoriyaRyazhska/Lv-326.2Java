package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.RelationConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.softserve.edu.cajillo.converter.impl.BoardConverterImpl;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.dto.RelationDto;
import com.softserve.edu.cajillo.dto.UserDto;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.Relation;
import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.enums.BoardType;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.entity.enums.RoleName;
import com.softserve.edu.cajillo.exception.BoardNotFoundException;
import com.softserve.edu.cajillo.exception.RelationServiceException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.RelationRepository;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class BoardServiceImpl implements BoardService {

    private static final String CAN_NOT_DELETE = "You can't delete this user, he is admin";

    @Value("${CLOUDINARYURL}")
    private String cloudUrl;

    @Autowired
    private BoardConverterImpl boardConverter;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamConverter teamConverter;

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

    @Autowired
    private UserService userService;

    public BoardDto createBoard(Board board, UserPrincipal userPrincipal) {

        board.setStatus(ItemsStatus.OPENED);
        Board save = boardRepository.save(board);
        relationRepository.save(relationConverter.convertToEntity(
                new RelationDto(
                        save.getId(),
                        userPrincipal.getId(),
                        RoleName.ADMIN,
                        null)
        ));
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
        if(!boardDto.getTableLists().isEmpty()) {
            sortTableListsBySequenceNumber(boardDto);
        }
        sprintService.sortSprintsBySequenceNumber(boardDto.getSprints());
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

    public List<BoardDto> getAllActiveBoardsByTeamId(Long teamId) {
        List<BoardDto> allBoardsForCurrentTeamWithDublicates = new ArrayList<>();
        List<Relation> allManagers = relationRepository.findAllByTeamId(teamId);
        if (allManagers != null) {
            for (Relation manager : allManagers) {
                System.out.println(manager.getBoard());
                if (manager.getBoard() != null) {
                    allBoardsForCurrentTeamWithDublicates.add(boardConverter.convertToDto(manager.getBoard()));
                }
            }
        }
        List<BoardDto> allBoardsForCurrentTeam = new ArrayList<>(
                new HashSet<>(allBoardsForCurrentTeamWithDublicates));
        return allBoardsForCurrentTeam;
    }

    @Override
    public BoardDto createNewTeamBoard(Long teamId, Board board) {
        Board currentBoard = null;
        if (teamService.getTeam(teamId) != null) {
            Map<User, RoleName> allUsersInTeam = relationService.getAllUsersInTeam(teamId);
            board.setStatus(ItemsStatus.OPENED);
            currentBoard = boardRepository.save(board);

            for (Map.Entry<User, RoleName> entry : allUsersInTeam.entrySet()) {
                User user = entry.getKey();
                RoleName role = entry.getValue();
                relationRepository.save(relationConverter.convertToEntity(
                        new RelationDto(
                                currentBoard.getId(),
                                user.getId(),
                                role,
                                teamId)
                ));
            }
            if (board.getBoardType().equals(BoardType.SCRUM)) {
                sprintService.createSprintBacklog(board.getId());
            }
        }
        return boardConverter.convertToDto(currentBoard);
    }

    @Override
    public void deleteTeamBoard(Long teamId, Long boardId) {
        List<Relation> allByBoardId = relationRepository.findAllByBoardId(boardId);
        if (allByBoardId.size() != 0) {
            for (Relation relation : allByBoardId) {
                if (relation.getTeam().getId() == teamId) {
                    if (relation.getRoleName().equals(RoleName.ADMIN)) {
                        relation.setTeam(null);
                        deleteBoard(boardId);
                    } else {
                        relationRepository.deleteById(relation.getId());
                    }
                }
            }
        } else {
            throw new BoardNotFoundException(String.format("Board with id %d not found", boardId));
        }

    }

    private void saveRelationForBoardsAdmin(Long teamId, Long boardId) {
        List<Relation> allByBoardId = relationRepository.findAllByBoardId(boardId);
        for (Relation relation : allByBoardId) {
            relation.setTeam(teamConverter.convertToEntity(teamService.getTeam(teamId)));
            relation.setRoleName(RoleName.ADMIN);
            relationRepository.save(relation);
        }
    }

    @Override
    public void addBoardToTeam(Long teamId, Long boardId) {
        Board currentBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(String.format("Board with id %d not found", boardId)));
        if (currentBoard.getStatus().equals(ItemsStatus.DELETED)) {
            currentBoard.setStatus(ItemsStatus.OPENED);
            recoverAllInternalItems(boardId);
            boardRepository.save(currentBoard);
            saveRelationForBoardsAdmin(teamId, boardId);
        } else {
            saveRelationForBoardsAdmin(teamId, boardId);
        }

        Map<User, RoleName> allUsersInTeam = relationService.getAllUsersInTeam(teamId);
        for (Map.Entry<User, RoleName> entry : allUsersInTeam.entrySet()) {
            User user = entry.getKey();
            RoleName role = entry.getValue();
            if (role.equals(RoleName.USER)) {
                relationRepository.save(relationConverter.convertToEntity(
                        new RelationDto(
                                currentBoard.getId(),
                                user.getId(),
                                role,
                                teamId)
                ));
            }
        }
    }

    public void saveBoardBackground(BoardDto boardDto) {
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
        Collections.sort(tableListDtos, new Comparator<TableListDto>() {
            @Override
            public int compare(TableListDto o1, TableListDto o2) {
                return o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
            }
        });
    }

    private void addUser(User newUserOnBoard, BoardDto currentBoard) {
        relationRepository.save(relationConverter.convertToEntity(
                new RelationDto(
                        currentBoard.getId(),
                        newUserOnBoard.getId(),
                        RoleName.USER,
                        null)
        ));
    }

    @Override
    public void addUserToBoard(Long boardId, UserDto userDto, UserPrincipal userPrincipal) {
        BoardDto currentBoard = getBoard(boardId);
        User newUserOnBoard = userService.getUserByEmail(userDto.getEmail());
        List<Relation> allByBoardId = relationRepository.findAllByBoardId(boardId);
        if (allByBoardId.size() == 0) {
            relationRepository.save(relationConverter.convertToEntity(
                    new RelationDto(
                            currentBoard.getId(),
                            userPrincipal.getId(),
                            RoleName.ADMIN,
                            null)
            ));
            addUser(newUserOnBoard, currentBoard);
        } else {
            addUser(newUserOnBoard, currentBoard);
        }
    }

    @Override
    public void deleteUserFromBoard(Long boardId, Long userId) {
        List<Relation> allByUserId = relationRepository.findAllByUserId(userId);
        for (Relation relation : allByUserId) {
            if (relation.getBoard().getId() == boardId
                    && relation.getRoleName() == RoleName.USER
                    && relation.getTeam() == null) {
                relationRepository.delete(relation);
            } else throw new RelationServiceException(CAN_NOT_DELETE);
        }
    }
}
