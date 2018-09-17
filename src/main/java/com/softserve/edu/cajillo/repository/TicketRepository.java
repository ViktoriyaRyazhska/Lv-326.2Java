package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Ticket;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findById(Long aLong);

    List<Ticket> findAllByTableListIdAndStatus(Long tableListId, ItemsStatus status);

    List<Ticket> findAllByTableListIdAndStatusAndSprintId(Long tableListId, ItemsStatus status, Long sprintId);

    List<Ticket> findAllBySprintId(Long sprintId);

    @Modifying
    @Query(value = "update tickets set sequence_number = sequence_number - 1 " +
            "where sequence_number >= :start and sequence_number <= :end", nativeQuery = true)
    void decrementTicket(@Param("start") int start, @Param("end") int end);

    @Modifying
    @Query(value = "update tickets set sequence_number = sequence_number + 1 " +
            "where sequence_number >= :start and sequence_number <= :end", nativeQuery = true)
    void incrementTicket(@Param("start") int start, @Param("end") int end);
}