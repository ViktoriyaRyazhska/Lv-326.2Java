package com.softserve.edu.cajillo.service.impl;

import com.cloudinary.Api;
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
import com.softserve.edu.cajillo.exception.RelationServiceException;
import com.softserve.edu.cajillo.exception.ResourceNotFoundException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.RelationRepository;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.cloudinary.json.JSONArray;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
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
        log.info("Creating board: " + board);
        return boardConverter.convertToDto(save);
    }

    public BoardDto updateBoard(Long id, Board board) {
        Board existedBoard = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        existedBoard.setName(board.getName());
        log.info(String.format("Updating board with id %d to: " + board, id));
        return boardConverter.convertToDto(boardRepository.save(existedBoard));
    }

    public BoardDto getBoard(Long id) {
        log.info(String.format("Getting board with id %d", id));
        Board board = boardRepository.findByIdAndStatus(id, ItemsStatus.OPENED)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        BoardDto boardDto = boardConverter.convertToDto(board);
        if (!boardDto.getTableLists().isEmpty()) {
            sortTableListsBySequenceNumber(boardDto);
        }
        sprintService.sortSprintsBySequenceNumber(boardDto.getSprints());
        return boardDto;
    }

    public Board getBoardEntity(Long id) {
        log.info(String.format("Getting board with id %d", id));
        Board board = boardRepository.findByIdAndStatus(id, ItemsStatus.OPENED)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        return board;
    }

    public void deleteBoard(Long id) {
        log.info(String.format("Deleting board with id %d", id));
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        deleteAllInternalBoardItems(id);
        board.setStatus(ItemsStatus.DELETED);
        boardRepository.save(board);
    }

    private void deleteAllInternalBoardItems(Long boardId) {
        log.info(String.format("Deleting all internal items in board %d", boardId));
        tableListService.deleteTableListsByBoardId(boardId);
        sprintService.archiveAllSprintsByBoard(boardId);

    }

    public BoardDto recoverBoard(Long boardId) {
        Board board = boardRepository.findByIdAndStatus(boardId, ItemsStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        log.info(String.format("Recovering board with id %d", boardId));
        board.setStatus(ItemsStatus.OPENED);
        recoverAllInternalItems(boardId);
        boardRepository.save(board);
        return boardConverter.convertToDto(board);
    }

    private void recoverAllInternalItems(Long boardId) {
        log.info(String.format("Recovering all internal items in board %d", boardId));
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
            throw new ResourceNotFoundException("Board", "id", boardId);
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
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
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
        log.info(String.format("Setting new image url to board %d", boardId));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", boardId));
        board.setImage(cloudImageUrl);
        boardRepository.save(board);
    }

    private String uploadImageOnCloud(byte[] imageFile, BoardDto boardDto) {
        log.info(String.format("Saving new background image to board %d on cloud", boardDto.getId()));
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
        log.info(String.format("Sorting lists in board with id %d", boardDto.getId()));
        Collections.sort(tableListDtos, (Comparator.comparing(TableListDto::getSequenceNumber)));
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

    @Override
    public List<String> getAllBackgroundImagesByBoardId(Long boarId) {
        Cloudinary cloudinary = new Cloudinary(cloudUrl);
        Api api = cloudinary.api();
        String jsonNext = null;
        boolean ifWeHaveMoreResources = true;
        ArrayList<String> listRes = new ArrayList<>();
        try {
            while (ifWeHaveMoreResources) {
                JSONObject outerObject = new JSONObject(
                        api.resources(ObjectUtils.asMap("max_results", 500, "next_cursor", jsonNext)));
                if (outerObject.has("next_cursor")) {
                    jsonNext = outerObject.get("next_cursor").toString();
                    ifWeHaveMoreResources = true;
                } else {
                    ifWeHaveMoreResources = false;
                }
                JSONArray jsonArray = outerObject.getJSONArray("resources");
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject objectInArray = jsonArray.getJSONObject(i);
                    String url = objectInArray.get("url").toString();
                    if (url.contains("/board_images/" + boarId)) {
                        listRes.add(url);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listRes;
    }

    @Transactional
    public void setExistingImageOnBackground(Long boardId, String imageUrl) {
        boardRepository.setExistingImageOnBackground(boardId, imageUrl);
    }

    @Transactional
    public void clearBoardBackground(Long boardId) {
        boardRepository.clearBoardBackground(boardId);
    }
}
