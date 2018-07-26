package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.TableList;
import com.softserve.edu.cajillo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Override
    Optional<Ticket> findById(Long aLong);


}
