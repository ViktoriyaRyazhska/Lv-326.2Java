package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.converter.impl.TableListConverterImpl;
import com.softserve.edu.cajillo.dto.TableListDto;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import com.softserve.edu.cajillo.service.impl.TableListServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TableListServiceImplTest {

    @Mock
    private TableListRepository tableListRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private TableListConverterImpl tableListConverter;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TableListServiceImpl tableListService;

    @Test
    public void updateTableListTest() {
        Mockito.when(tableListRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(tableListInstance()));
        TableList tableList = tableListInstance();
        tableList.setName("123");
        Mockito.when(tableListRepository.save(Mockito.any())).thenReturn(tableList);
        Mockito.when(tableListConverter.convertToDto(tableList)).thenReturn(tableListDtoInstance());
        TableListDto tableListDto = tableListService.updateTableList(Long.valueOf(1), Long.valueOf(1), tableListInstance());
        assertEquals(tableListDto.getName(), tableList.getName());
    }

    @Test
    public void createTableListTest() {
        Mockito.when(boardRepository.findByIdAndStatus(Long.valueOf(1), ItemsStatus.OPENED))
                .thenReturn(Optional.of(boardInstance()));
        Mockito.when(tableListRepository.getMaxSequenceValue(Mockito.anyLong())).thenReturn(Long.valueOf(10));
        TableList tableList = tableListInstance();
        tableList.setName("123");
        Mockito.when(tableListRepository.save(Mockito.any())).thenReturn(tableList);
        Mockito.when(tableListConverter.convertToDto(Mockito.any(TableList.class))).thenReturn(tableListDtoInstance());
        TableListDto tableList1 = tableListService.createTableList(Long.valueOf(1), tableListInstance());
        assertEquals(tableList1.getName(), tableList.getName());
    }

    @Test
    public void getAllTableListsTest() {
        Mockito.when(tableListRepository.findAllByBoardIdAndStatus(Mockito.anyLong(), Mockito.any(ItemsStatus.class)))
                .thenReturn(tableListsInstance());
        Mockito.when(tableListConverter.convertToDto(Mockito.anyList())).thenReturn(tableListsDtosInstance());
        List<TableListDto> allTableLists = tableListService.getAllTableLists(Long.valueOf(1));
        assertEquals(allTableLists.get(0).getBoardId(), Long.valueOf(1));
    }

    @Test
    public void getTableListTest() {
        Mockito.when(tableListRepository.findByIdAndStatus(Mockito.anyLong(), Mockito.any(ItemsStatus.class)))
                .thenReturn(Optional.of(tableListInstance()));
        Mockito.when(tableListConverter.convertToDto(Mockito.any(TableList.class))).thenReturn(tableListDtoInstance());
        TableListDto tableList = tableListService.getTableList(Long.valueOf(1));
        assertEquals(tableList.getName(), tableListInstance().getName());
    }


    private TableList tableListInstance() {
        TableList tableList = new TableList();
        tableList.setName("123");
        return tableList;
    }

    private TableListDto tableListDtoInstance() {
        TableListDto tableListDto = new TableListDto();
        tableListDto.setName("123");
        return tableListDto;
    }

    private List<TableList> tableListsInstance() {
        List<TableList> tableLists = new ArrayList<>();
        tableLists.add(new TableList());
        tableLists.add(new TableList());
        Board board = new Board();
        board.setId(Long.valueOf(1));
        for (TableList tableList : tableLists) {
            tableList.setBoard(board);
        }
        return tableLists;
    }

    private List<TableListDto> tableListsDtosInstance() {
        List<TableListDto> tableLists = new ArrayList<>();
        tableLists.add(new TableListDto());
        tableLists.add(new TableListDto());
        Board board = new Board();
        board.setId(Long.valueOf(1));
        for (TableListDto tableList : tableLists) {
            tableList.setBoardId(Long.valueOf(1));
        }
        return tableLists;
    }

    private Board boardInstance() {
        return new Board();
    }
}
