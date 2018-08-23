package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Sprint;
import com.softserve.edu.cajillo.entity.enums.SprintStatus;
import com.softserve.edu.cajillo.entity.enums.SprintType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

    @Override
    Optional<Sprint> findById(Long sprintId);

    List<Sprint> getAllByBoardIdAndSprintStatusNot
            (Long boardId, SprintStatus sprintStatus);

    Sprint getByBoardIdAndSprintType
            (Long boardId, SprintType sprintType);

    List<Sprint> getAllByBoardIdAndSprintStatusNotAndSprintType
            (Long boardId, SprintStatus sprintStatus, SprintType sprintType);

    @Query(value = "SELECT max(sequence_number) FROM sprints where board_id = :id", nativeQuery = true)
    Long getMaxSequenceValue(@Param("id") Long id);

    List<Sprint> findByBoardIdAndSequenceNumberGreaterThan(Long boardId, Integer sequenceNumber);

    @Modifying
    @Query(value = "update sprints set sequence_number = sequence_number - 1 " +
            "where sequence_number >= :start and sequence_number <= :end", nativeQuery = true)
    void decrementSprints(@Param("start") int start, @Param("end") int end);

    @Modifying
    @Query(value = "update sprints set sequence_number = sequence_number + 1 " +
            "where sequence_number >= :start and sequence_number <= :end", nativeQuery = true)
    void incrementSprints(@Param("start") int start, @Param("end") int end);
}
