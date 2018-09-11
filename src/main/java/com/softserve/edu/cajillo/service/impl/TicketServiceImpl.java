package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.converter.ticketConverter.*;
import com.softserve.edu.cajillo.dto.*;
import com.softserve.edu.cajillo.entity.*;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import com.softserve.edu.cajillo.exception.ResourceNotFoundException;
import com.softserve.edu.cajillo.repository.*;
import com.softserve.edu.cajillo.security.CurrentUser;
import com.softserve.edu.cajillo.security.UserPrincipal;
import com.softserve.edu.cajillo.service.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketToBoardResponseDtoConverter ticketToBoardResponseDtoConverter;

    @Autowired
    private TicketToCreateTicketDtoConverter ticketToCreateTicketDtoConverter;

    @Autowired
    private TicketConverter ticketConverter;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Override
    public TicketDto getTicket(Long id) {
        return ticketConverter.convertToDto(getTicketByTicketId(id));
    }

    @Override
    public TicketDto updateTicketWithMap(Map<String, String> updates) {
        Ticket ticket = getTicketByTicketId(Long.valueOf(updates.get("id")));
        modelMapper.map(updates, ticket);

        if (updates.containsKey("assignedToId")) {
            ticket.setAssignedTo(getUserByUserId(Long.valueOf(updates.get("assignedToId"))));
        }

        if (updates.containsKey("tableList")) {
            ticket.setTableList(getTableListByTableListId(Long.valueOf(updates.get("tableList"))));
        }

        if (updates.containsKey("boardId")) {
            ticket.setBoard(getBoardByBoardId(Long.valueOf(updates.get("boardId"))));
        }

        if (updates.containsKey("sprintId")) {
            ticket.setSprint(getSprintBySprintId(Long.valueOf(updates.get("sprintId"))));
        }

        if (updates.containsKey("expirationDate")) {
            ticket.setExpirationDate(Instant.parse(updates.get("expirationDate")));
        }
        return ticketConverter.convertToDto(ticketRepository.save(ticket));
    }

    @Override
    public void deleteTicket(Long ticketId) {
        Ticket ticket = getTicketByTicketId(ticketId);
        ticket.setStatus(ItemsStatus.DELETED);
        ticketRepository.save(ticket);
    }

    @Override
    public CreateTicketDto createTicket(CreateTicketDto createTicketRequest, @CurrentUser UserPrincipal userPrincipal) {
        createTicketRequest.setCreatedById(userPrincipal.getId());
        Ticket ticket = ticketToCreateTicketDtoConverter.convertToEntity(createTicketRequest);
        ticket.setStatus(ItemsStatus.OPENED);
        return ticketToCreateTicketDtoConverter.convertToDto(ticketRepository.save(ticket));
    }

    @Override
    public List<TicketForBoardResponseDto> getTicketsByListId(Long tableListId) {
        return sortTicketsBySequenceNumber(ticketToBoardResponseDtoConverter
                .convertToDto(ticketRepository.findAllByTableListIdAndStatus(tableListId, ItemsStatus.OPENED)));
    }

    @Override
    public List<TicketForBoardResponseDto> getTicketsBySprintId(Long sprintId) {
        return sortTicketsBySequenceNumber(ticketToBoardResponseDtoConverter
                .convertToDto(ticketRepository.findAllBySprintId(sprintId)));
    }

    @Override
    public TicketDto updateTicket(TicketDto ticketDto) {
        return ticketConverter.convertToDto(ticketRepository.save(ticketConverter.convertToEntity(ticketDto)));
    }

    @Override
    public void deleteTicketsByTableListId(Long listId) {
        List<Ticket> allByTableListId = ticketRepository.findAllByTableListIdAndStatus(listId, ItemsStatus.OPENED);
        for (Ticket ticket : allByTableListId) {
            ticket.setStatus(ItemsStatus.DELETED);
            ticketRepository.save(ticket);
        }
    }

    @Override
    public void recoverTicketsByListId(Long listId) {
        List<Ticket> tickets = ticketRepository.findAllByTableListIdAndStatus(listId, ItemsStatus.DELETED);
        for (Ticket ticket : tickets) {
            ticket.setStatus(ItemsStatus.OPENED);
            ticketRepository.save(ticket);
        }
    }

    @Override
    public void recoverTicketsBySprintId(Long sprintId) {
        List<Ticket> tickets = ticketRepository.findAllBySprintId(sprintId);
        for (Ticket ticket : tickets) {
            ticket.setStatus(ItemsStatus.OPENED);
            ticketRepository.save(ticket);
        }
    }

    @Override
    public void archiveTicketsBySprintId(Long sprintId) {
        List<Ticket> tickets = ticketRepository.findAllBySprintId(sprintId);
        for (Ticket ticket : tickets) {
            ticket.setStatus(ItemsStatus.DELETED);
            ticketRepository.save(ticket);
        }
    }

    @Override
    @Transactional
    public void updateTicketSequenceNumber(OrderTicketDto orderTicketDto) {
        Ticket ticket = getTicketByTicketId(orderTicketDto.getTicketId());
        if(orderTicketDto.getTableListId() != null)
        ticket.setTableList(getTableListByTableListId(orderTicketDto.getTableListId()));
        if(orderTicketDto.getSprintId() != null)
        ticket.setSprint(getSprintBySprintId(orderTicketDto.getSprintId()));
        if (ticket.getSequenceNumber() < orderTicketDto.getSequenceNumber()) {
            ticketRepository.decrementTicket(ticket.getSequenceNumber() + 1, orderTicketDto.getSequenceNumber());
            ticket.setSequenceNumber(orderTicketDto.getSequenceNumber());
            ticketRepository.save(ticket);
        } else if (ticket.getSequenceNumber() > orderTicketDto.getSequenceNumber()) {
            ticketRepository.incrementTicket(orderTicketDto.getSequenceNumber(), ticket.getSequenceNumber() - 1);
            ticket.setSequenceNumber(orderTicketDto.getSequenceNumber());
            ticketRepository.save(ticket);
        }
    }

    private Comparator<TicketForBoardResponseDto> compareBySequenceNumber() {
        return new Comparator<TicketForBoardResponseDto>() {
            @Override
            public int compare(TicketForBoardResponseDto ticketForBoardResponseDto, TicketForBoardResponseDto t1) {
                return ticketForBoardResponseDto.getSequenceNumber() - t1.getSequenceNumber();
            }
        };
    }

    private List<TicketForBoardResponseDto> sortTicketsBySequenceNumber(List<TicketForBoardResponseDto> ticketForBoardResponseDtos) {
        if (ticketForBoardResponseDtos != null) {
            ticketForBoardResponseDtos.sort(compareBySequenceNumber());
        }
        return ticketForBoardResponseDtos;
    }

    private Ticket getTicketByTicketId(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(() ->
                new ResourceNotFoundException("Ticket", "id", ticketId));
    }

    private Sprint getSprintBySprintId(Long sprintId) {
        return sprintRepository.findById(sprintId).orElseThrow(() ->
                new ResourceNotFoundException("Sprint", "id", sprintId));
    }

    private Board getBoardByBoardId(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() ->
                new ResourceNotFoundException("Board", "id", boardId));
    }

    private TableList getTableListByTableListId(Long tableListId) {
        return tableListRepository.findById(tableListId).orElseThrow(() ->
                new ResourceNotFoundException("Table list", "id", tableListId));
    }

    private User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId));
    }
}