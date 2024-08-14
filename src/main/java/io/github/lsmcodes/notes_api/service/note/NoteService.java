package io.github.lsmcodes.notes_api.service.note;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.github.lsmcodes.notes_api.model.note.Note;

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
     * Checks whether a note exists based on the provided id.
     * 
     * @param id The id to be searched for.
     * @return {@code true} if the note exists; {@code false} otherwise.
     */
    boolean existsById(UUID id);

    /**
     * Finds a note based on the provided id.
     * 
     * @param id The id of the note to be searched for.
     * @return An {@link Optional} containing the retrieved note if found, or
     *         {@code Optional.empty()} if no note is found.
     */
    Optional<Note> findById(UUID id);

    /**
     * Retrieves all notes stored in the database.
     * 
     * @return A {@link List} of all notes.
     */
    List<Note> findAll();

    /**
     * Finds notes where the title or content contains the provided term.
     * 
     * @param term The term to be searched for.
     * @return An {@link List} of notes containing the term, or
     *         {@code Collections.emptyList()} if no notes match.
     */
    List<Note> findByTitleOrContentContainingIgnoreCase(String term);

    /**
     * Finds notes that have at least one tag from the specified list of tags.
     * 
     * @param tags A {@link List} of tags to be searched for.
     * @return A {@link List} of notes with any of the specified tags, or
     *         {@code Collections.emptyList()} if no notes have matching tags.
     */
    List<Note> findByTagsInIgnoreCase(List<String> tags);

    /**
     * Deletes a note based on the provided id.
     * 
     * @param id The id of the note to be deleted.
     */
    void deleteById(UUID id);

}