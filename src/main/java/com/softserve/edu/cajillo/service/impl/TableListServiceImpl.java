package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.impl.TableListConverterImpl;
import com.softserve.edu.cajillo.dto.OrderTableListDto;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.exception.BoardNotFoundException;
import com.softserve.edu.cajillo.exception.TableListNotFoundException;
import com.softserve.edu.cajillo.exception.UnsatisfiedException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import com.softserve.edu.cajillo.service.TableListService;
import com.softserve.edu.cajillo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TableListServiceImpl implements TableListService {

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TableListConverterImpl tableListConverter;

    @Autowired
    private TicketService ticketService;

    public TableListDto createTableList(Long id, TableList tableList) {
        Board board = boardRepository.findByIdAndStatus(id, ItemsStatus.OPENED)
                .orElseThrow(() -> new BoardNotFoundException(String.format("Board with id %d not found", id)));
        tableList.setBoard(board);
        tableList.setStatus(ItemsStatus.OPENED);
        Long maxSequenceValue = tableListRepository.getMaxSequenceValue(id);
        tableList.setSequenceNumber(Math.toIntExact((maxSequenceValue == null) ? 0 : ++maxSequenceValue));
        tableListRepository.save(tableList);
        return tableListConverter.convertToDto(tableList);
    }

    public void deleteTableList(Long listId) {
        TableList tableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new TableListNotFoundException(String.format("TableList with id %d not found", listId)));
        decrementNextTableLists(tableList.getBoard().getId(), listId);
        tableList.setSequenceNumber(null);
        tableList.setStatus(ItemsStatus.DELETED);
        tableListRepository.save(tableList);
        deleteAllInternalTickets(listId);
    }

    private void deleteAllInternalTickets(Long listId) {
        ticketService.deleteTicketsByTableListId(listId);
    }

    public void recoverTableListsByBoardId(Long boardId) {
        List<TableList> lists = tableListRepository.findAllByBoardIdAndStatus(boardId, ItemsStatus.DELETED);
        for (TableList list : lists) {
            list.setStatus(ItemsStatus.OPENED);
            tableListRepository.save(list);
            ticketService.recoverTicketsByListId(list.getId());
        }
    }

    public void deleteTableListsByBoardId(Long boardId) {
        List<TableList> allByBoardIdAndStatus = tableListRepository
                .findAllByBoardIdAndStatus(boardId, ItemsStatus.OPENED);
        for (TableList tableList : allByBoardIdAndStatus) {
            tableList.setStatus(ItemsStatus.DELETED);
            tableListRepository.save(tableList);
            ticketService.deleteTicketsByTableListId(tableList.getId());
        }
    }

    public TableListDto updateTableList(Long listId, Long boardId, TableList tableList) {
        TableList existingList = tableListRepository.findById(listId)
                .orElseThrow(() -> new TableListNotFoundException(String.format("TableList with id %d not found", listId)));
        existingList.setName(tableList.getName());
        return tableListConverter.convertToDto(tableListRepository.save(existingList));
    }

    public List<TableListDto> getAllTableLists(Long boardId) {
        List<TableList> allByBoardId = tableListRepository.findAllByBoardIdAndStatus(boardId, ItemsStatus.OPENED);
        return tableListConverter.convertToDto(allByBoardId);
    }

    public TableListDto getTableList(Long listId) {
        TableList tableList = tableListRepository.findByIdAndStatus(listId, ItemsStatus.OPENED)
                .orElseThrow(() -> new TableListNotFoundException(String.format("TableList with id %d not found", listId)));
        return tableListConverter.convertToDto(tableList);
    }

    public void decrementNextTableLists(Long boardId, Long listId) {
        TableList tableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new TableListNotFoundException(String.format("TableList with id %d not found", listId)));
        List<TableList> boards = tableListRepository
                .findByBoardIdAndSequenceNumberGreaterThan(boardId, tableList.getSequenceNumber());
        for (TableList board : boards) {
            board.setSequenceNumber(board.getSequenceNumber() - 1);
        }
    }

    @Transactional
    public void updateListOrdering(OrderTableListDto orderTableListDto) {
        TableList tableList = tableListRepository.findByIdAndStatus(orderTableListDto.getListId(), ItemsStatus.OPENED)
                .orElseThrow(() -> new TableListNotFoundException(
                        String.format("Table list with id %d not found", orderTableListDto.getListId())));
        if(tableList.getSequenceNumber() < orderTableListDto.getSequenceNumber()) {
            decrementAllIntermediateLists(tableList.getSequenceNumber() + 1, orderTableListDto.getSequenceNumber());
            tableList.setSequenceNumber(orderTableListDto.getSequenceNumber());
            tableListRepository.save(tableList);
        } else if(tableList.getSequenceNumber() > orderTableListDto.getSequenceNumber()) {
            incrementAllIntermediateLists(orderTableListDto.getSequenceNumber(), tableList.getSequenceNumber() - 1);
            tableList.setSequenceNumber(orderTableListDto.getSequenceNumber());
            tableListRepository.save(tableList);
        }
    }

    private void decrementAllIntermediateLists(int startId, int endId) {
        tableListRepository.decrementTableLists(startId, endId);
    }

    private void incrementAllIntermediateLists(int startId, int endId) {
        tableListRepository.incrementTableLists(startId, endId);
    }

}