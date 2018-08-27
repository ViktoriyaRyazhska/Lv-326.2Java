package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.impl.TableListConverterImpl;
import com.softserve.edu.cajillo.dto.OrderTableListDto;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.exception.ResourceNotFoundException;
import com.softserve.edu.cajillo.exception.TableListNotFoundException;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import com.softserve.edu.cajillo.service.TableListService;
import com.softserve.edu.cajillo.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
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
        log.info(String.format("Creating table list with id %d: " + tableList, id));
        Board board = boardRepository.findByIdAndStatus(id, ItemsStatus.OPENED)
                .orElseThrow(() -> new ResourceNotFoundException("Board", "id", id));
        tableList.setBoard(board);
        tableList.setStatus(ItemsStatus.OPENED);
        Long maxSequenceValue = tableListRepository.getMaxSequenceValue(id);
        tableList.setSequenceNumber(Math.toIntExact((maxSequenceValue == null) ? 0 : ++maxSequenceValue));
        tableListRepository.save(tableList);
        return tableListConverter.convertToDto(tableList);
    }

    public void deleteTableList(Long listId) {
        log.info(String.format("Deleting table list with id %d", listId));
        TableList tableList = tableListRepository.findById(listId)
                .orElseThrow(() -> new TableListNotFoundException(String.format("TableList with id %d not found", listId)));
        decrementNextTableLists(tableList.getBoard().getId(), listId);
        tableList.setSequenceNumber(null);
        tableList.setStatus(ItemsStatus.DELETED);
        tableListRepository.save(tableList);
        deleteAllInternalTickets(listId);
    }

    private void deleteAllInternalTickets(Long listId) {
        log.info(String.format("Deleting all internal elements in table list with id %d", listId));
        ticketService.deleteTicketsByTableListId(listId);
    }

    public void recoverTableListsByBoardId(Long boardId) {
        log.info(String.format("Recovering all table lists in board with id %d", boardId));
        List<TableList> lists = tableListRepository.findAllByBoardIdAndStatus(boardId, ItemsStatus.DELETED);
        for (TableList list : lists) {
            list.setStatus(ItemsStatus.OPENED);
            tableListRepository.save(list);
            ticketService.recoverTicketsByListId(list.getId());
        }
    }

    public void deleteTableListsByBoardId(Long boardId) {
        log.info(String.format("Deleting all table lists in board with id %d", boardId));
        List<TableList> allByBoardIdAndStatus = tableListRepository
                .findAllByBoardIdAndStatus(boardId, ItemsStatus.OPENED);
        for (TableList tableList : allByBoardIdAndStatus) {
            tableList.setStatus(ItemsStatus.DELETED);
            tableListRepository.save(tableList);
            ticketService.deleteTicketsByTableListId(tableList.getId());
        }
    }

    public TableListDto updateTableList(Long listId, Long boardId, TableList tableList) {
        log.info(String.format("Updating table list with id %d: " + tableList, listId));
        TableList existingList = tableListRepository.findById(listId)
                .orElseThrow(() -> new TableListNotFoundException(String.format("TableList with id %d not found", listId)));
        existingList.setName(tableList.getName());
        return tableListConverter.convertToDto(tableListRepository.save(existingList));
    }

    public List<TableListDto> getAllTableLists(Long boardId) {
        log.info(String.format("Getting all table lists where board id %d", boardId));
        List<TableList> allByBoardId = tableListRepository.findAllByBoardIdAndStatus(boardId, ItemsStatus.OPENED);
        return tableListConverter.convertToDto(allByBoardId);
    }

    public TableListDto getTableList(Long listId) {
        log.info(String.format("Getting table list with id %d", listId));
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
        log.info("Updating list ordering");
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