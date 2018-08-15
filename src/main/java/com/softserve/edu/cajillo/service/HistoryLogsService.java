package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.HistoryLogDto;
import com.softserve.edu.cajillo.security.UserPrincipal;

import java.util.List;

public interface HistoryLogsService {
    HistoryLogDto createLog(HistoryLogDto historyLogDto, UserPrincipal userPrincipal);

    List<HistoryLogDto> getTwentyLogsByBoardId(Long boardId, Long lastLogId);

    Long getCountLogs();
}
