package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.HistoryLogDto;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.HistoryLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class HistoryLogsController {

    @Autowired
    private HistoryLogsService historyLogsService;

    @PostMapping
    public HistoryLogDto createLog(@RequestBody HistoryLogDto historyLogDto,
                                   @CurrentUser UserPrincipal userPrincipal) {
        return historyLogsService.createLog(historyLogDto, userPrincipal);
    }

    @GetMapping("/{boardId}/{lastLogId}")
    public List<HistoryLogDto> getMoreLogs(@PathVariable("boardId") Long boardId,
                                           @PathVariable("lastLogId") Long lastLogId) {
        return historyLogsService.getTwentyLogsByBoardId(boardId, lastLogId);
    }
}
