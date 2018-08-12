package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.HistoryLogDto;
import com.softserve.edu.cajillo.service.HistoryLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/log")
public class HistoryLogsController {

    @Autowired
    private HistoryLogsService historyLogsService;

    @PostMapping
    public HistoryLogDto createLog(@RequestBody HistoryLogDto historyLogDto) {
        return historyLogsService.createLog(historyLogDto);
    }
}
