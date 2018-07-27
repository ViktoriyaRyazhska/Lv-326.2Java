package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

//    @Query(value = "select * from boards where id = :id", nativeQuery = true)
//    Optional<Board> findById(@Param("id") Long id);

    @Override
    Optional<Board> findById(Long aLong);

}
