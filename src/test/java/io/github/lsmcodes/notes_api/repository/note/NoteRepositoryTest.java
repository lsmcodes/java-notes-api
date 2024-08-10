package io.github.lsmcodes.notes_api.repository.note;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.model.note.Note;
import jakarta.persistence.EntityManager;

/**
 * Tests NoteRepository methods
 */
@DataJpaTest
@ActiveProfiles("test")
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * Tests the repository method for saving notes
     */
    @Test
    @Order(1)
    @DisplayName("Save should save note")
    public void save_ShouldSaveNote() {
        // Arrange
        Note note = Note.builder()
                .tags(List.of("Tag"))
                .title("First Note")
                .content("Sample content")
                .build();

        // Act
        Note savedNote = this.noteRepository.save(note);

        // Assert
        assertThat(savedNote)
                .isNotNull()
                .isEqualTo(note);
    }

    /**
     * Tests the repository method for finding notes by title
     */
    @Test
    @Order(2)
    @DisplayName("Find by title should return correct note")
    public void findByTitle_ShouldReturnCorrectNote() {
        // Arrange
        Note note = getNewNote();

        // Act
        Note foundNote = this.noteRepository.findByTitle("Untitled").getFirst();

        // Assert
        assertThat(note).isEqualTo(foundNote);
    }

    /**
     * Tests the repository method for finding notes by tags
     */
    @Test
    @Order(3)
    @DisplayName("Find by tags in should return correct notes")
    public void findByTagsIn_ShouldReturnCorrectNotes() {
        // Arrange
        List<Note> notes = List.of(getNewNote(), getNewNote());

        // Act
        List<Note> foundNotesByTag = this.noteRepository.findByTagsIn(List.of("Tag"));
        List<Note> foundNotesByTags = this.noteRepository.findByTagsIn(List.of("Tag", "Another Tag"));

        // Assert
        assertThat(foundNotesByTag)
                .hasSize(2)
                .containsExactlyInAnyOrderElementsOf(notes);

        assertThat(foundNotesByTags)
                .hasSize(2)
                .containsExactlyInAnyOrderElementsOf(notes);
    }

    /**
     * Tests the repository method for finding notes by creation date
     */
    @Test
    @Order(4)
    @DisplayName("Find by created at should return correct note")
    public void findByCreatedAt_ShouldReturnCorrectNote() {
        // Arrange
        Note note = getNewNote();
        this.entityManager.flush();

        // Act
        LocalDateTime creationDate = note.getCreatedAt();
        Note foundNote = this.noteRepository.findByCreatedAt(creationDate).getFirst();

        // Assert
        assertThat(note).isEqualTo(foundNote);
    }

    /**
     * Tests the repository method for finding notes within a creation date range
     */
    @Test
    @Order(5)
    @DisplayName("Find by created at between should return correct note")
    public void findByCreatedAtBetween_ShouldReturnCorrectNote() {
        // Arrange
        Note note = getNewNote();
        this.entityManager.flush();

        // Act
        LocalDateTime creationDate = note.getCreatedAt();
        Note foundNote = this.noteRepository.findByCreatedAtBetween(creationDate,
                creationDate.plusDays(2)).getFirst();

        // Assert
        assertThat(note).isEqualTo(foundNote);
    }

    /**
     * Creates and saves a Note object for use in tests
     * 
     * @return Note
     */
    private Note getNewNote() {
        Note note = Note.builder()
                .tags(List.of("Tag", "Another Tag"))
                .title("Untitled")
                .content("Sample content")
                .build();
        return this.noteRepository.save(note);
    }

}