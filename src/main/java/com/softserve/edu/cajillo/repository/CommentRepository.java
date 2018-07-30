package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Comment;
import com.softserve.edu.cajillo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

//    @Override
    List<Comment> findByTicketId(Long aLong);

}
