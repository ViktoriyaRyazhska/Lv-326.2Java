package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.HistoryLogConverter;
import com.softserve.edu.cajillo.dto.HistoryLogDto;
import com.softserve.edu.cajillo.entity.HistoryLog;
import com.softserve.edu.cajillo.repository.HistoryLogsRepository;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.HistoryLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryLogsServiceImpl implements HistoryLogsService {

    @Autowired
    private HistoryLogsRepository historyLogsRepository;

    @Autowired
    private HistoryLogConverter historyLogConverter;

    public HistoryLogDto createLog(HistoryLogDto historyLogDto, UserPrincipal userPrincipal) {
        historyLogDto.setUsername(userPrincipal.getUsername());
        historyLogDto.setUserId(userPrincipal.getId());
        HistoryLog historyLog = historyLogConverter.convertToEntity(historyLogDto);
        historyLogsRepository.save(historyLog);
        return historyLogConverter.convertToDto(historyLog);
    }

    public List<HistoryLogDto> getTwentyLogsByBoardId(Long boardId, Long lastLogId) {
        List<HistoryLog> allByBoardId = historyLogsRepository
                .findTop20ByBoardIdAndIdLessThanOrderByIdDesc(boardId, lastLogId);
        return historyLogConverter.convertToDto(allByBoardId);
    }

    public Long getCountLogs() {
        return historyLogsRepository.count();
    }
}
