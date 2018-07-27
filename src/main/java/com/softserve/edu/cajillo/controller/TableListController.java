package com.softserve.edu.cajillo.controller;

import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.service.TableListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TableListController {

    @Autowired
    TableListService tableListService;

    @PostMapping("/api/boards/{id}/lists")
    public TableListDto createTableList(@PathVariable Long id, @RequestBody TableList tableList) {
        return tableListService.createTableList(id, tableList);
    }

    @DeleteMapping("/api/boards/{id}/lists/{listId}")
    public void deleteTableList(@PathVariable("listId") Long listId) {
        tableListService.deleteTableList(listId);
    }

    @PutMapping("/api/boards/{id}/lists/{listId}")
    public TableListDto updateTableList(@PathVariable("listId") Long listId, @PathVariable("id") Long boardId,
                                        @RequestBody TableList tableList) {
        return tableListService.updateTableList(listId, boardId, tableList);
    }
}
