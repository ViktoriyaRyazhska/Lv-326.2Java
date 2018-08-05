package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Sprint;
import com.softserve.edu.cajillo.entity.enums.SprintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

    @Override
    Optional<Sprint> findById(Long sprintId);

    Optional<List<Sprint>> getAllByBoardId(Long boardId);

    Optional<List<Sprint>> getAllByBoardAndSprintStatus(Long boardId, SprintStatus sprintStatus);

}
