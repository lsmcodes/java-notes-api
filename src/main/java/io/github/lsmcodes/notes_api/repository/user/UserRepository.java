package io.github.lsmcodes.notes_api.repository.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.lsmcodes.notes_api.model.user.User;

/**
* Implements a User repository with CRUD JPA methods and customized methods
*/
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Verifies if a user exist by username
     */
    boolean existsByUsername(String username);

    /**
     * Searches an user by username
     */
    Optional<User> findByUsername(String username);

}