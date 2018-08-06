package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.service.TableListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TableListController {

    @Autowired
    private TableListService tableListService;

    @PostMapping("/api/boards/{boardId}/lists")
    public TableListDto createTableList(@PathVariable Long boardId, @RequestBody TableList tableList) {
        return tableListService.createTableList(boardId, tableList);
    }

    @DeleteMapping("/api/lists/{listId}")
    public void deleteTableList(@PathVariable("listId") Long listId) {
        tableListService.deleteTableList(listId);
    }

    @PutMapping("/api/boards/{boardId}/lists/{listId}")
    public TableListDto updateTableList(@PathVariable("listId") Long listId, @PathVariable("boardId") Long boardId,
                                        @RequestBody TableList tableList) {
        return tableListService.updateTableList(listId, boardId, tableList);
    }

    @GetMapping("/api/boards/{boardId}/lists")
    public List<TableListDto> getAllTableLists(@PathVariable("boardId") Long boardId) {
        return tableListService.getAllTableLists(boardId);
    }

    @GetMapping("/api/boards/lists/{listId}")
    public TableListDto getTableList(@PathVariable("listId") Long listId) {
        return tableListService.getTableList(listId);
    }

    @PutMapping("/api/boards/lists/{listId1}/{listId2}")
    public List<TableListDto> swapSequenceNumbers(@PathVariable Long listId1, @PathVariable Long listId2) {
        return tableListService.swapSequenceNumbers(listId1, listId2);
    }
}