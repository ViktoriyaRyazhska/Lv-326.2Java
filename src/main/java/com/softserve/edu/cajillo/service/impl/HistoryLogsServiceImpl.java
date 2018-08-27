package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.HistoryLogConverter;
import com.softserve.edu.cajillo.dto.HistoryLogDto;
import com.softserve.edu.cajillo.entity.HistoryLog;
import com.softserve.edu.cajillo.repository.HistoryLogsRepository;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.HistoryLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class HistoryLogsServiceImpl implements HistoryLogsService {

    @Autowired
    private HistoryLogsRepository historyLogsRepository;

    @Autowired
    private HistoryLogConverter historyLogConverter;

    public HistoryLogDto createLog(HistoryLogDto historyLogDto, UserPrincipal userPrincipal) {
        log.info(String.format("Creating log: " + historyLogDto + " for user " + userPrincipal.getUsername()));
        historyLogDto.setUsername(userPrincipal.getUsername());
        historyLogDto.setUserId(userPrincipal.getId());
        HistoryLog historyLog = historyLogConverter.convertToEntity(historyLogDto);
        historyLogsRepository.save(historyLog);
        return historyLogConverter.convertToDto(historyLog);
    }

    public List<HistoryLogDto> getTwentyLogsByBoardId(Long boardId, Long lastLogId) {
        log.info(String.format("Getting more board logs for board with id %d", boardId));
        List<HistoryLog> allByBoardId = historyLogsRepository
                .findTop20ByBoardIdAndIdLessThanOrderByIdDesc(boardId, lastLogId);
        return historyLogConverter.convertToDto(allByBoardId);
    }

    public Long getCountLogs()
    {
        log.info("Getting count of logs");
        return historyLogsRepository.count();
    }

}
