package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.HistoryLogDto;

import java.util.List;

public interface HistoryLogsService {
    HistoryLogDto createLog(HistoryLogDto historyLogDto);

    List<HistoryLogDto> getAllLogsByBoardId(Long boardId);
}
