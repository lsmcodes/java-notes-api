package io.github.lsmcodes.notes_api.repository.note;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;

/**
 * Implements a Note repository with CRUD JPA methods and customized methods.
 */
public interface NoteRepository extends JpaRepository<Note, UUID> {

    /**
     * Verifies if a note exists by user and id.
     * 
     * @param user The note owner.
     * @param id   The note id to be verified.
     * @return {@code true} if the note exists; {@code false} otherwise.
     */
    boolean existsByUserAndId(User user, UUID id);

    /**
     * Finds a note by user and id.
     * 
     * @param user The note owner.
     * @param id   The note id to be searched for.
     * @return An {@link Optional} containing the note if found, or
     *         {@code Optional.empty()} if no note is found.
     */
    @Query("SELECT n FROM notes n JOIN FETCH n.tags " +
            "WHERE n.user = :user AND n.id = :id")
    Optional<Note> findByUserAndId(User user, UUID id);

    /**
     * Finds a {@link Page} of notes by user.
     * 
     * @param user     The notes owner.
     * @param pageable The pagination and sorting information.
     * @return A {@link Page} of notes sorted by creation date.
     */
    @Query("SELECT n FROM notes n JOIN FETCH n.tags " +
            "WHERE n.user = :user")
    Page<Note> findByUser(User user, Pageable pageable);

    /**
     * Finds notes based on the provided user that contain the specified term in
     * either the title or content, ignoring case.
     * 
     * @param user     The notes owner.
     * @param term     The term to be searched for in the title or content of the
     *                 notes.
     * @param pageable The pagination and sorting information.
     * @return A {@link Page} of notes that contain the specified term in the title
     *         or content.
     */
    @Query("SELECT n FROM notes n JOIN FETCH n.tags " +
            "WHERE n.user = :user AND LOWER(n.title) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(n.content) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Note> findByUserAndTitleOrContentContainingIgnoreCase(@Param("user") User user, @Param("term") String term,
            Pageable pageable);

    /**
     * Finds notes based on the provided user that have at least one of the
     * specified tags, ignoring case.
     * 
     * @param user     The notes owner.
     * @param tags     The {@link List} of tags to be searched for in the notes.
     * @param pageable The pagination and sorting information.
     * @return A {@link Page} of notes that contain at least one of the specified
     *         tags.
     */
    @Query("SELECT n FROM notes n JOIN FETCH n.tags t " +
            "WHERE n.user = :user " +
            "AND LOWER(t) IN (:tags)")
    Page<Note> findByUserAndTagsInIgnoreCase(User user, List<String> tags, Pageable pageable);

    /**
     * Deletes a note based on the provided user and id.
     * 
     * @param user The notes owner.
     * @param id   The id of the note to be deleted.
     */
    void deleteByUserAndId(User user, UUID id);

    /**
     * Delete notes based on the provided user.
     * 
     * @param user The notes owner.
     */
    void deleteByUser(User user);

}