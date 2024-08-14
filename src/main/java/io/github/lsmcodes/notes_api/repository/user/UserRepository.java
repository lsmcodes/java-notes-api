package io.github.lsmcodes.notes_api.repository.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.lsmcodes.notes_api.model.user.User;

/**
 * Implements a User repository with CRUD JPA methods and customized methods.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Verifies if an user exists by the provided username.
     * 
     * @param username The username of the user to be searched for.
     * @return {@code true} if the user exists, {@code false} otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Finds an user by the provided username.
     * 
     * @param username The username of the user to be searched for.
     * @return An {@link Optional} containing the user if found, otherwise an
     *         {@code Optional.empty()} if no user is found.
     */
    Optional<User> findByUsername(String username);

}