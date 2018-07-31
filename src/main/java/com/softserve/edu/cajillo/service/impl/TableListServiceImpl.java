package com.softserve.edu.cajillo.service.impl;

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
public class TableListServiceImpl {

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
        Long maxSequenceValue = tableListRepository.getMaxSequenceValue(id);
        Long nextSequenceNumber = (maxSequenceValue == null) ? 1 : ++maxSequenceValue;
        tableList.setSequenceNumber(Math.toIntExact(nextSequenceNumber));
        tableListRepository.save(tableList);
        return tableListConverter.convertToDto(tableList);
    }

    public void deleteTableList(Long listId) {
        TableList tableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new UnsatisfiedException(String.format("TableList with id %d not found", listId)));
        decrementNextTableLists(tableList.getBoard().getId(), listId);
        tableList.setSequenceNumber(null);
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

    public void decrementNextTableLists(Long boardId, Long listId) {
        TableList tableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new UnsatisfiedException(String.format("TableList with id %d not found", listId)));
        List<TableList> boards = tableListRepository
                .findByBoardIdAndSequenceNumberGreaterThan(boardId, tableList.getSequenceNumber());
        for (TableList board : boards) {
            board.setSequenceNumber(board.getSequenceNumber() - 1);
        }
    }

    public List<TableListDto> swapSequenceNumbers(Long listId1, Long listId2) {
        TableList tableList1 = tableListRepository.findById(listId1)
                .orElseThrow(() -> new UnsatisfiedException(String.format("TableList with id %d not found", listId1)));
        TableList tableList2 = tableListRepository.findById(listId2)
                .orElseThrow(() -> new UnsatisfiedException(String.format("TableList with id %d not found", listId2)));
        swapNumbers(tableList1, tableList2);
        tableListRepository.save(tableList1);
        tableListRepository.save(tableList2);
        List<TableList> tableLists = tableListRepository.findAllByBoardIdAndStatus(tableList1.getBoard().getId(), ItemsStatus.OPENED);
        return tableListConverter.convertToDto(tableLists);
    }

    public void swapNumbers(TableList tableList1, TableList tableList2) {
        int number = tableList1.getSequenceNumber();
        tableList1.setSequenceNumber(tableList2.getSequenceNumber());
        tableList2.setSequenceNumber(number);
    }
}