package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findById(Long aLong);

    @Override
    void deleteById(Long aLong);
}