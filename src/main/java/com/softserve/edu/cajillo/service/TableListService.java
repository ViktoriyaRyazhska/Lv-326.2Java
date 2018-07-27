package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.converter.impl.TableListConverterImpl;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class TableListService {

    @Autowired
    TableListRepository tableListRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    TableListConverterImpl tableListConverter;

    public TableListDto createTableList(Long id, TableList tableList) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NullPointerException(String.format("Board id = %d not found", id)));
        tableList.setBoard(board);
        tableListRepository.save(tableList);
        return tableListConverter.convertToDto(tableList);
    }

    public void deleteTableList(Long listId) {
        tableListRepository.deleteById(listId);
    }

    public TableListDto updateTableList(Long listId, Long boardId, TableList tableList) {
        tableList.setId(listId);
        Instant createTimeById = tableListRepository.findCreateTimeById(listId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NullPointerException(String.format("Board id = %d not found", boardId)));
        tableList.setBoard(board);
        tableListRepository.save(tableList);
        TableList resultTableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new NullPointerException(String.format("TableList with id %d not found", listId)));
        resultTableList.setCreateTime(createTimeById);
        return tableListConverter.convertToDto(resultTableList);
    }

    public List<TableListDto> getAllTableLists(Long boardId) {
        List<TableList> allByBoardId = tableListRepository.findAllByBoardId(boardId);
        return tableListConverter.convertToDto(allByBoardId);
    }

    public TableListDto getTableList(Long listId) {
        TableList tableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new NullPointerException(String.format("TableList with id %d not found", listId)));
        return tableListConverter.convertToDto(tableList);
    }
}
