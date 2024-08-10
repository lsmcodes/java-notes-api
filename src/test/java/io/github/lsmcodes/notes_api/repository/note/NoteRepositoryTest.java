package io.github.lsmcodes.notes_api.repository.note;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.model.note.Note;

/**
 * Integration tests for the {@link NoteRepository} interface.
 */
@ActiveProfiles("test")
@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    /**
     * Tests the save repository method to ensure it correctly saves a note to the
     * database.
     */
    @Test
    @Order(1)
    @DisplayName("NoteRepository save method should save note")
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
     * Tests the existsById repository method to ensure it correctly verifies if a
     * note exists in the database by the provided id.
     */
    @Test
    @Order(2)
    @DisplayName("NoteRepository existsById method should return true when note exists")
    public void existsById_ShouldReturnTrue_WhenNoteExists() {
        // Arrange
        Note note = getNewNote();

        // Act
        boolean noteExists = this.noteRepository.existsById(note.getId());

        // Assert
        assertThat(noteExists).isTrue();
    }

    /**
     * Tests the findById repository method to ensure it correctly retrieves a note
     * by the provided id from the database.
     */
    @Test
    @Order(3)
    @DisplayName("NoteRepository findById method should return correct note")
    public void findById_ShouldReturnCorrectNote() {
        // Arrange
        Note note = getNewNote();

        // Act
        Optional<Note> foundNote = this.noteRepository.findById(note.getId());

        // Assert
        assertThat(foundNote).isPresent();
        assertThat(foundNote.get()).isEqualTo(note);
    }

    /**
     * Tests the findAll repository method to ensure it correctly retrieves all
     * notes from the database.
     */
    @Test
    @Order(4)
    @DisplayName("NoteRepository findAll method should return all notes")
    public void findAll_ShouldReturnAllNotes() {
        // Arrange
        List<Note> notes = List.of(getNewNote(), getNewNote());

        // Act
        List<Note> foundNotes = this.noteRepository.findAll();

        // Assert
        assertThat(foundNotes)
                .hasSize(2)
                .containsAll(notes);
    }

    /**
     * Tests the
     * {@link NoteRepository#findByTitleOrContentContainingIgnoreCase(String term)}
     * method to ensure it finds notes whose title or content contains the specified
     * term, ignoring case.
     */
    @Test
    @Order(5)
    @DisplayName("NoteRepository findByTitleOrContentContainingIgnoreCase method should return correct note")
    public void findByTitleOrContentContainingIgnoreCase_ShouldReturnCorrectNote() {
        // Arrange
        List<Note> notes = List.of(getNewNote(), getNewNote());

        // Act
        List<Note> foundNotes = this.noteRepository.findByTitleOrContentContainingIgnoreCase("sample");

        // Assert
        assertThat(foundNotes)
                .hasSize(2)
                .containsAll(notes);
    }

    /**
     * Tests the {@link NoteRepository#findByTagsInIgnoreCase(List<String> tags)}
     * method to ensure it finds notes whose tags contains at least one of the
     * specified tags, ignoring case.
     */
    @Test
    @Order(6)
    @DisplayName("NoteRepository findByTagsInIgnoreCase method should return correct notes")
    public void findByTagsInIgnoreCase_ShouldReturnCorrectNotes() {
        // Arrange
        List<Note> notes = List.of(getNewNote(), getNewNote());

        // Act
        List<Note> foundNotesByTag = this.noteRepository.findByTagsInIgnoreCase(List.of("tag"));
        List<Note> foundNotesByTags = this.noteRepository.findByTagsInIgnoreCase(List.of("Tag", "Another tag"));

        // Assert
        assertThat(foundNotesByTag)
                .hasSize(2)
                .containsAll(notes);

        assertThat(foundNotesByTags)
                .hasSize(2)
                .containsAll(notes);
    }

    /**
     * Tests the deleteById repository method to ensure it correctly deletes a note
     * correctly by the provided id from the database.
     */
    @Test
    @Order(7)
    @DisplayName("NoteRepository deleteById method should delete correct note")
    public void deleteById_ShouldDeleteCorrectNote() {
        // Arrange
        Note note = getNewNote();
        UUID noteId = note.getId();

        // Act and Assert
        assertThat(this.noteRepository.existsById(noteId)).isTrue();
        this.noteRepository.deleteById(noteId);
        assertThat(this.noteRepository.existsById(noteId)).isFalse();
    }

    /**
     * Creates and saves a generic {@link Note} instance for use in tests.
     * 
     * @return The saved {@link Note} instance.
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