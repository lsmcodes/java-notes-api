package io.github.lsmcodes.notes_api.service.note;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;

/**
 * Provides methods for manipulating Note objects.
 */
public interface NoteService {

    /**
     * Saves the provided Note object to the database.
     * 
     * @param note The Note object to be saved.
     * @return The saved Note object.
     */
    Note save(Note note);

    /**
     * Checks whether a note exists based on the provided user and id.
     * 
     * @param user The owner of the note.
     * @param id   The id of the note to be searched for.
     * @return {@code true} if the note exists; {@code false} otherwise.
     */
    boolean existsByUserAndId(User user, UUID id);

    /**
     * Retrieves a note based on the provided user and id.
     * 
     * @param user The owner of the note.
     * @param id   The id of the note to be searched for.
     * @return An {@link Optional} containing the retrieved note if found, or
     *         {@code Optional.empty()} if no note is found.
     */
    Optional<Note> findByUserAndId(User user, UUID id);

    /**
     * Retrieves a {@link Page} of notes from the provided user.
     * 
     * @param user     The owner of the notes.
     * @param pageable The pagination and sorting information.
     * @return A {@link Page} of notes sorted by creation date, or
     *         {@code Page.empty()} if there is no notes in the database.
     */
    Page<Note> findByUser(User user, Pageable pageable);

    /**
     * Retrieves notes based on the provided user where the title or content
     * contains the provided term.
     * 
     * @param user     The owner of the notes.
     * @param term     The term to be searched for.
     * @param pageable The pagination and sorting information.
     * @return An {@link Page} of notes containing the term, or
     *         {@code Page.empty()} if no notes match.
     */
    Page<Note> findByUserAndTitleOrContentContainingIgnoreCase(User user, String term, Pageable pageable);

    /**
     * Retrieves notes based on the provided user that have at least one tag from
     * the specified list of tags.
     * 
     * @param user     The owner of the notes.
     * @param tags     A {@link List} of tags to be searched for.
     * @param pageable The pagination and sorting information.
     * @return A {@link Page} of notes with any of the specified tags, or
     *         {@code Page.empty()} if no notes have matching tags.
     */
    Page<Note> findByUserAndTagsInIgnoreCase(User user, List<String> tags, Pageable pageable);

    /**
     * Deletes a note based on the provided user and id.
     * 
     * @param user The owner of the note.
     * @param id   The id of the note to be deleted.
     */
    void deleteByUserAndId(User user, UUID id);

    /**
     * Deletes notes based on the provided user.
     * 
     * @param user
     */
    void deleteByUser(User user);

}