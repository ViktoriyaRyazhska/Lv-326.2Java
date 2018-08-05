package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long aLong);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}