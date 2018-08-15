package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {

    Optional<Relation> findById(Long aLong);

    Optional<List<Relation>> findAllByUserId(Long id);

    List<Relation> findAllByTeamId(Long id);
}