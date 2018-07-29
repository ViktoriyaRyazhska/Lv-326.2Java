package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.TableList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

@Repository
public interface TableListRepository extends JpaRepository<TableList, Long>, JpaSpecificationExecutor<TableList> {
    @Modifying
    @Query(value = "delete from table_lists where id = :listId", nativeQuery = true)
    void deleteById(@Param("listId") Long id);

    @Override
    Optional<TableList> findById(Long aLong);

    List<TableList> findAllByBoardId(Long boardId);

    @Query(value = "select create_time from table_lists where id = :id", nativeQuery = true)
    Instant findCreateTimeById(@Param("id") Long id);
}
