package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.HistoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryLogsRepository extends JpaRepository<HistoryLog, Long> {

    List<HistoryLog> findTop20ByBoardIdAndIdLessThanOrderByIdDesc(Long boardId, Long lastLogId);

}
