package io.github.lsmcodes.notes_api.service.verification;

import java.util.UUID;

import org.springframework.data.domain.Page;

import io.github.lsmcodes.notes_api.exception.NoteNotFoundException;
import io.github.lsmcodes.notes_api.exception.UserNotFoundException;
import io.github.lsmcodes.notes_api.exception.UsernameAlreadyExistsException;
import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;

/**
 * Provides methods for verifying various conditions related to users and notes.
 */
public interface VerificationService {

    /**
     * Verifies if a user exists by username.
     * 
     * @param username The username of the user to be searched for.
     * @throws UserNotFoundException If there is no user with the provided username.
     */
    public void verifyIfUserExistsByUsername(String username) throws UserNotFoundException;

    /**
     * Verifies if a username does not exists.
     * 
     * @param username The username to be searched for.
     * @throws UsernameAlreadyExistsException If the username already exists in the
     *                                        database.
     */
    public void verifyIfUsernameDoesNotExist(String username) throws UsernameAlreadyExistsException;

    /**
     * Verifies if the found {@link Page} is not empty.
     * 
     * @param notes The found {@link Page}.
     * @throws NoteNotFoundException If the found {@link Page} is empty.
     */
    public void verifyIfPageOfNotesIsNotEmpty(Page<Note> notes) throws NoteNotFoundException;

    /**
     * Verifies if a note exists by user and id.
     * 
     * @param user The owner of the note.
     * @param id   The id of the note to be searched for.
     * @throws NoteNotFoundException If no note is found.
     */
    public void verifyIfNoteExistsByUserAndId(User user, UUID id) throws NoteNotFoundException;

    /**
     * Verifies if the current user is authenticated.
     * 
     * @return {@code true} if the current user is authenticated; {@code false}
     *         otherwise.
     */
    public boolean verifyIfCurrentUserIsAuthenticated();

    /**
     * Verifies if the current user is not authenticated.
     * 
     * @return {@code true} if the current user is not authenticated; {@code false}
     *         otherwise.
     */
    public boolean verifyIfCurrentUserIsNotAuthenticated();

}