package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.TableList;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TableListRepository extends JpaRepository<TableList, Long>, JpaSpecificationExecutor<TableList> {
    @Modifying
    @Query(value = "delete from table_lists where id = :listId", nativeQuery = true)
    void deleteById(@Param("listId") Long id);

//    @Query(value = "select * from table_lists where id = :id", nativeQuery = true)
//    Optional<TableList> findById(@Param("id") Long id);

    List<TableList> findAllByBoardId(Long boardId);

    @Query(value = "select create_time from table_lists where id = :id", nativeQuery = true)
    Instant findCreateTimeById(@Param("id") Long id);
}
