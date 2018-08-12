package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.HistoryLogConverter;
import com.softserve.edu.cajillo.dto.HistoryLogDto;
import com.softserve.edu.cajillo.entity.HistoryLog;
import com.softserve.edu.cajillo.repository.HistoryLogsRepository;
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

    public HistoryLogDto createLog(HistoryLogDto historyLogDto) {
        HistoryLog historyLog = historyLogConverter.convertToEntity(historyLogDto);
        historyLogsRepository.save(historyLog);
        return historyLogConverter.convertToDto(historyLog);
    }

    public List<HistoryLogDto> getAllLogsByBoardId(Long boardId) {
        List<HistoryLog> allByBoardId = historyLogsRepository.findAllByBoardId(boardId);
        return historyLogConverter.convertToDto(allByBoardId);
    }
}
