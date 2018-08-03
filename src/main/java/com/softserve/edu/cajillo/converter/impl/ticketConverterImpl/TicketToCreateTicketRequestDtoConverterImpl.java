package com.softserve.edu.cajillo.converter.impl.ticketConverterImpl;

import com.softserve.edu.cajillo.converter.ticketConverter.TicketToCreateTicketRequestDtoConverter;
import com.softserve.edu.cajillo.dto.CreateTicketRequestDto;
import com.softserve.edu.cajillo.dto.CreateTicketResponseDto;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import com.softserve.edu.cajillo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketToCreateTicketRequestDtoConverterImpl implements TicketToCreateTicketRequestDtoConverter {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private UserService userService;

    @Override
    public Ticket convertToEntity(CreateTicketRequestDto dto) {
        Ticket ticket = modelMapper.map(dto, Ticket.class);

        ticket.setBoard(boardRepository.findById(dto.getBoardId()).orElseThrow(RuntimeException::new));
        ticket.setTableList(tableListRepository.findById(dto.getTableListId()).orElseThrow(RuntimeException::new));
        ticket.setCreatedBy(userService.getUser(dto.getCreatedById()));
        System.out.println(ticket.toString());

        return ticket;
    }

    @Override
    public CreateTicketRequestDto convertToDto(Ticket entity) {
        return modelMapper.map(entity, CreateTicketRequestDto.class);
    }
}