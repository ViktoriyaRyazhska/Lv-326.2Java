package com.softserve.edu.cajillo.service.impl;

import com.softserve.edu.cajillo.dto.CreateTicketRequest;
import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.repository.BoardRepository;
import com.softserve.edu.cajillo.repository.TableListRepository;
import com.softserve.edu.cajillo.repository.TicketRepository;
import com.softserve.edu.cajillo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TableListRepository tableListRepository;

    @Autowired
    private BoardRepository boardRepository;

    public Ticket createTicket(CreateTicketRequest createTicketRequest) {

        // Creating user's account
        Ticket ticket = new Ticket();

        ticket.setName(createTicketRequest.getName());
        ticket.setTableList(tableListRepository.findById(createTicketRequest.getTableListId()).orElseThrow(RuntimeException::new));
        ticket.setBoard(boardRepository.findById(createTicketRequest.getBoardId()).orElseThrow(RuntimeException::new));


//        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
//                .orElseThrow(() -> new AppException("User Role not set."));

//        user.setRoles(Collections.singleton(userRole));

        Ticket result = ticketRepository.save(ticket);
        return result;

//        URI location = ServletUriComponentsBuilder
//                .fromCurrentContextPath().path("/api/users/{username}")
//                .buildAndExpand(result.getId()).toUri();
//
//        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));

    }

}
