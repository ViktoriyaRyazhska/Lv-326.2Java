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

    List<Sprint> getAllByBoardIdAndSprintStatusNot
            (Long boardId, SprintStatus sprintStatus);

    List<Sprint> getAllByBoardIdAndSprintStatus(Long boardId, SprintStatus sprintStatus);

}
