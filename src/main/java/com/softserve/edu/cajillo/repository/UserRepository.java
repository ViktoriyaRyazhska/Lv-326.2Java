package com.softserve.edu.cajillo.repository;

import com.softserve.edu.cajillo.entity.User;
import com.softserve.edu.cajillo.security.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long aLong);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Query(value = "update users set chosen_language = :lang where id = :id", nativeQuery = true)
    void changeChosenLanguage(@Param("lang") String language, @Param("id") Long id);
}