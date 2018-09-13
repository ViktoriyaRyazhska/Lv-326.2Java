//package com.softserve.edu.cajillo.service;
//
//import com.softserve.edu.cajillo.converter.HistoryLogConverter;
//import com.softserve.edu.cajillo.dto.HistoryLogDto;
//import com.softserve.edu.cajillo.entity.Board;
//import com.softserve.edu.cajillo.entity.HistoryLog;
//import com.softserve.edu.cajillo.entity.User;
//import com.softserve.edu.cajillo.repository.HistoryLogsRepository;
//import com.softserve.edu.cajillo.security.UserPrincipal;
//import com.softserve.edu.cajillo.service.impl.HistoryLogsServiceImpl;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class HistoryLogsServiceImplTest {
//
//    @Mock
//    private HistoryLogsRepository historyLogsRepository;
//
//    @Mock
//    private HistoryLogConverter historyLogConverter;
//
//    @InjectMocks
//    private HistoryLogsServiceImpl historyLogsService;
//
//    @Test
//    public void testGetCountLog() {
//        Mockito.when(historyLogsRepository.count()).thenReturn(Long.valueOf(configureLogsList().size()));
//        Long number = historyLogsService.getCountLogs();
//        assertEquals(number, Long.valueOf(3));
//    }
//
//    @Test
//    public void testCreateLog() {
//        Mockito.when(historyLogsRepository.save(generateHistoryLog())).thenReturn(generateHistoryLog());
//        Mockito.when(historyLogConverter.convertToDto(generateHistoryLog())).thenReturn(generatehistoryLogDto());
//        HistoryLogDto log = historyLogsService.createLog(generatehistoryLogDto(), generateUserPrintical());
//        assertNull(log);
//    }
//
//    private List<HistoryLogDto> configureLogsList() {
//        List<HistoryLogDto> historyLogDtos = new ArrayList<>();
//        historyLogDtos.add(new HistoryLogDto());
//        historyLogDtos.add(new HistoryLogDto());
//        historyLogDtos.add(new HistoryLogDto());
//        return historyLogDtos;
//    }
//
//    private HistoryLogDto generatehistoryLogDto() {
//        HistoryLogDto historyLogDto = new HistoryLogDto();
//        historyLogDto.setUserId(Long.valueOf(1));
//        historyLogDto.setMessage("123");
//        return historyLogDto;
//    }
//
//    private HistoryLog generateHistoryLog() {
//        HistoryLog historyLog = new HistoryLog();
//        historyLog.setUser(Mockito.mock(User.class));
//        historyLog.setBoard(Mockito.mock(Board.class));
//        historyLog.setMessage("123");
//        return historyLog;
//    }
//
//    private UserPrincipal generateUserPrintical() {
//        UserPrincipal userPrincipal = new UserPrincipal();
//        userPrincipal.setUsername("asd");
//        userPrincipal.setId(Long.valueOf(333));
//        return userPrincipal;
//    }
//}
