package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.converter.RelationConverter;
import com.softserve.edu.cajillo.converter.TeamConverter;
import com.softserve.edu.cajillo.converter.impl.BoardConverterImpl;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.RelationRepository;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;


public class BoardServiceImplTest {

    @Mock
    private String cloudUrl;

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
}
