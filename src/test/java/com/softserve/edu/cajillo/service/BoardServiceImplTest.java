package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.converter.RelationConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.converter.impl.BoardConverterImpl;
import com.softserve.edu.cajillo.dto.BoardDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.RelationRepository;
import com.softserve.edu.cajillo.service.impl.BoardServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class BoardServiceImplTest {

    @Mock
    private BoardConverterImpl boardConverter;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private TeamConverter teamConverter;

    @Mock
    private RelationRepository relationRepository;

    @Mock
    private RelationService relationService;

    @Mock
    private RelationConverter relationConverter;

    @Mock
    private TableListService tableListService;

    @Mock
    private SprintService sprintService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BoardServiceImpl boardService;

    @Test
    public void updateBoardTest() {
        Mockito.when(boardRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(boardInstance()));
        Mockito.when(boardRepository.save(Mockito.any(Board.class))).thenReturn(boardInstance());
        Mockito.when(boardConverter.convertToDto(Mockito.any(Board.class))).thenReturn(boardDtoInstance());
        BoardDto boardDto = boardService.updateBoard(Long.valueOf(1), boardInstance());
    }

    private Board boardInstance() {
        Board board = new Board();
        board.setName("123");
        return board;
    }

    private BoardDto boardDtoInstance() {
        BoardDto boardDto = new BoardDto();
        boardDto.setName("123");
        return boardDto;
    }
}
