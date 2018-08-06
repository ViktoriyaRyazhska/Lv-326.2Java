package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTicketId(Long aLong);

}