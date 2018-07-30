package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.converter.impl.TableListConverterImpl;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.exception.UnsatisfiedException;
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
                .orElseThrow(() -> new UnsatisfiedException(String.format("Board id = %d not found", id)));
        tableList.setBoard(board);
        tableList.setStatus(ItemsStatus.OPENED);
        tableListRepository.save(tableList);
        return tableListConverter.convertToDto(tableList);
    }

    public void deleteTableList(Long listId) {
        TableList tableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new UnsatisfiedException(String.format("TableList with id %d not found", listId)));
        tableList.setStatus(ItemsStatus.DELETED);
        tableListRepository.save(tableList);
    }

    public TableListDto updateTableList(Long listId, Long boardId, TableList tableList) {
        tableList.setId(listId);
        Instant createTimeById = tableListRepository.findCreateTimeById(listId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new UnsatisfiedException(String.format("Board id = %d not found", boardId)));
        tableList.setBoard(board);
        tableListRepository.save(tableList);
        TableList resultTableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new UnsatisfiedException(String.format("TableList with id %d not found", listId)));
        resultTableList.setCreateTime(createTimeById);
        return tableListConverter.convertToDto(resultTableList);
    }

    public List<TableListDto> getAllTableLists(Long boardId) {
        List<TableList> allByBoardId = tableListRepository.findAllByBoardIdAndStatus(boardId, ItemsStatus.OPENED);
        List<TableListDto> tableListDtos = tableListConverter.convertToDto(allByBoardId);
        return tableListDtos;
    }

    public TableListDto getTableList(Long listId) {
        TableList tableList = tableListRepository.findByIdAndStatus(listId, ItemsStatus.OPENED)
                .orElseThrow(() -> new UnsatisfiedException(String.format("TableList with id %d not found", listId)));
        return tableListConverter.convertToDto(tableList);
    }
}
