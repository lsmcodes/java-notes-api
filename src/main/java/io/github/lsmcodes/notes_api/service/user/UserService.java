package io.github.lsmcodes.notes_api.service.user;

import java.util.Optional;
import java.util.UUID;

import io.github.lsmcodes.notes_api.model.user.User;

/**
 * Provides methods for manipulating User objects.
 */
public interface UserService {

    /**
     * Saves the provided User object to the database.
     * 
     * @param user The User object to be saved.
     * @return The saved User object.
     */
    User save(User user);

    /**
     * Checks whether an user exists based on the provided username.
     * 
     * @param username The username to be searched for.
     * @return {@code true} if the user exists; {@code false} otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Finds an user based on the provided id.
     * 
     * @param id The id of the user to be searched for.
     * @return An {@link Optional} containing the retrieved user if found, or
     *         {@code Optional.empty()} if no user is found.
     */
    Optional<User> findById(UUID id);

    /**
     * Finds an user based on the provided username.
     * 
     * @param username The username of the user to be searched for.
     * @return An {@link Optional} containing the retrieved user if found, or
     *         {@code Optional.empty()} if no user is found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Deletes an user based on the provided id.
     * 
     * @param id The id of the user to be deleted.
     */
    void deleteById(UUID id);

}