package io.github.lsmcodes.notes_api.repository.note;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.lsmcodes.notes_api.model.note.Note;

/**
 * Implements a Note repository with CRUD JPA methods and customized methods.
 */
public interface NoteRepository extends JpaRepository<Note, UUID> {

    /**
     * Finds notes that contain the specified term in either the title or content,
     * ignoring case.
     * 
     * @param term The term to be searched for in the title or content of the notes.
     * @return A {@link List} of notes that contain the specified term in the title
     *         or content.
     */
    @Query(value = "SELECT * FROM notes n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(n.content) LIKE LOWER(CONCAT('%', ?1, '%'))", nativeQuery = true)
    List<Note> findByTitleOrContentContainingIgnoreCase(String term);

    /**
     * Finds notes that have at least one of the specified tags, ignoring case.
     * 
     * @param tags The {@link List} of tags to be searched for in the notes.
     * @return A {@link List} of notes that contain at least one of the specified
     *         tags.
     */
    List<Note> findByTagsInIgnoreCase(List<String> tags);

}