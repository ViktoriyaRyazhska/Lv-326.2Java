package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.RoleManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleManagerRepository extends JpaRepository<RoleManager, Long> {

    @Override
    Optional<RoleManager> findById(Long aLong);

    List<RoleManager> findAllByUser_Id(Long id);
}
