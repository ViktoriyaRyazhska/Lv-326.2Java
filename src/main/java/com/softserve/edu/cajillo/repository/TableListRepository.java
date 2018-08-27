package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.TableList;

import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

@Repository
public interface TableListRepository extends JpaRepository<TableList, Long> {

    Optional<TableList> findByIdAndStatus(Long aLong, ItemsStatus status);

    List<TableList> findAllByBoardIdAndStatus(Long id, ItemsStatus status);

    @Query(value = "select create_time from table_lists where id = :id", nativeQuery = true)
    Instant findCreateTimeById(@Param("id") Long id);

    @Query(value = "SELECT max(sequence_number) FROM table_lists where board_id = :id", nativeQuery = true)
    Long getMaxSequenceValue(@Param("id") Long id);

    List<TableList> findByBoardIdAndSequenceNumberGreaterThan(Long id, Integer sequenceNumber);

    @Modifying
    @Query(value = "update table_lists set sequence_number = sequence_number - 1 " +
            "where sequence_number >= :start and sequence_number <= :end", nativeQuery = true)
    void decrementTableLists(@Param("start") int start, @Param("end") int end);

    @Modifying
    @Query(value = "update table_lists set sequence_number = sequence_number + 1 " +
            "where sequence_number >= :start and sequence_number <= :end", nativeQuery = true)
    void incrementTableLists(@Param("start") int start, @Param("end") int end);
}