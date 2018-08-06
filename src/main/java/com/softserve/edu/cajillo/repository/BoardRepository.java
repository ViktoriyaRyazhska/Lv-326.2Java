package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long aLong);

    Board findByIdAndStatus(Long id, ItemsStatus status);
}