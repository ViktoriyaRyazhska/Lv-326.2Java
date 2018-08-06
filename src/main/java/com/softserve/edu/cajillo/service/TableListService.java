package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.TableList;

import java.util.List;

public interface TableListService {

    TableListDto createTableList(Long id, TableList tableList);

    void deleteTableList(Long listId);

    TableListDto updateTableList(Long listId, Long boardId, TableList tableList);

    List<TableListDto> getAllTableLists(Long boardId);

    TableListDto getTableList(Long listId);

    void decrementNextTableLists(Long boardId, Long listId);

    List<TableListDto> swapSequenceNumbers(Long listId1, Long listId2);

    void swapNumbers(TableList tableList1, TableList tableList2);

    void deleteTableListsByBoardId(Long boardId);

    void recoverTableListsByBoardId(Long boardId);
}