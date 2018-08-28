package com.softserve.edu.cajillo.service;

import com.softserve.edu.cajillo.converter.impl.TableListConverterImpl;
import com.softserve.edu.cajillo.dto.OrderTableListDto;
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
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableListServiceImplVoidTest {

    @Mock
    private TableListRepository tableListRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private TableListConverterImpl tableListConverter;

    @Mock
    private TicketService ticketService;

    @Spy
    @InjectMocks
    private TableListServiceImpl tableListService;

    @Test(expected = Exception.class)
    public void updateListOrderingTest1() throws Exception {
        PowerMockito.when(tableListService, "decrementAllIntermediateLists").thenThrow(new Exception());
        PowerMockito.when(tableListRepository.findByIdAndStatus(Mockito.anyLong(), Mockito.any(ItemsStatus.class)))
                .thenReturn(Optional.ofNullable(tableListInstance()));
        tableListService.updateListOrdering(orderTableListDto(3));
    }

    @Test(expected = Exception.class)
    public void updateListOrderingTest2() throws Exception {
        PowerMockito.when(tableListService, "incrementAllIntermediateLists").thenThrow(new Exception());
        PowerMockito.when(tableListRepository.findByIdAndStatus(Mockito.anyLong(), Mockito.any(ItemsStatus.class)))
                .thenReturn(Optional.ofNullable(tableListInstance()));
        tableListService.updateListOrdering(orderTableListDto(7));
    }

    private TableList tableListInstance() {
        TableList tableList = new TableList();
        tableList.setSequenceNumber(1);
        return tableList;
    }

    private List<TableList> tableListsInstance() {
        List<TableList> tableLists = new ArrayList<>();
        tableLists.add(new TableList());
        tableLists.add(new TableList());
        Board board = new Board();
        board.setId(Long.valueOf(1));
        return tableLists;
    }

    private OrderTableListDto orderTableListDto(Integer sequenceNumber) {
        OrderTableListDto orderTableListDto = new OrderTableListDto();
        orderTableListDto.setSequenceNumber(sequenceNumber);
        return orderTableListDto;
    }
}
