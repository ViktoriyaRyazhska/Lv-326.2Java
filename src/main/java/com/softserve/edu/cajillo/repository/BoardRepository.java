package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Board;
import com.softserve.edu.cajillo.entity.enums.ItemsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findById(Long aLong);

    Optional<Board> findByIdAndStatus(Long id, ItemsStatus status);

    @Modifying
    @Query(value = "update boards set image_url = :imageUrl where id = :boardId", nativeQuery = true)
    void setExistingImageOnBackground(@Param("boardId") Long boardId, @Param("imageUrl") String imageUrl);

    @Modifying
    @Query(value = "update boards set image_url = null where id = :boardId", nativeQuery = true)
    void clearBoardBackground(@Param("boardId") Long boardId);
}