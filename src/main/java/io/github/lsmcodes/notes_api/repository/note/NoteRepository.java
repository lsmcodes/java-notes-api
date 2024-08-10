package io.github.lsmcodes.notes_api.repository.note;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.lsmcodes.notes_api.model.note.Note;

/**
* Implements a Note repository with CRUD JPA methods and customized methods
*/
public interface NoteRepository extends JpaRepository<Note, UUID> {

    /**
     * Searches notes by title
     */
    List<Note> findByTitle(String title);

    /**
     * Searches notes by tags
     */
    List<Note> findByTagsIn(List<String> tags);

    /**
     * Searches notes by creation date
     */
    List<Note> findByCreatedAt(LocalDateTime createdAt);

    /**
     * Searches notes within a creation date range
     */
    List<Note> findByCreatedAtBetween(LocalDateTime initialDate, LocalDateTime finalDate);

}