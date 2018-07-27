package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableListService {

    @Autowired
    TableListRepository tableListRepository;

    @Autowired
    BoardRepository boardRepository;

    public TableListDto createTableList(Long id, TableList tableList) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NullPointerException(String.format("Board id = %d not found", id)));
        tableList.setBoard(board);
        tableListRepository.save(tableList);
        return new TableListDto(tableList.getId(), tableList.getName(), tableList.getCreateTime(),
                tableList.getUpdateTime(), tableList.getBoard().getId(), tableList.getBoard().getName());
    }

    public void deleteTableList(Long listId) {
        tableListRepository.deleteById(listId);
    }

    public TableListDto updateTableList(Long listId, Long boardId, TableList tableList) {
        tableList.setId(listId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NullPointerException(String.format("Board id = %d not found", boardId)));
        tableList.setBoard(board);
        tableListRepository.save(tableList);
        TableList returnedTableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new NullPointerException(String.format("BoardList id = %d not found", listId)));
        return new TableListDto(returnedTableList.getId(), returnedTableList.getName(), returnedTableList.getCreateTime(),
                returnedTableList.getUpdateTime(), tableList.getBoard().getId(), tableList.getBoard().getName());
    }

    public List<TableListDto> getAllTableLists(Long boardId) {
        List<TableList> allByBoardId = tableListRepository.findAllByBoardId(boardId);
        return null;
    }
}
