package io.github.lsmcodes.notes_api.service.note.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.repository.note.NoteRepository;

/**
 * Unit tests for the {@link NoteServiceImpl} class.
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteServiceImpl;

    /**
     * Tests the {@link NoteServiceImpl#save(Note note)} method to ensure it
     * correctly saves a note to the database.
     */
    @Test
    @Order(1)
    @DisplayName("NoteServiceImpl save method should save note")
    public void save_ShouldSaveNote() {
        // Arrange
        Note note = getNewNote();
        Mockito.when(this.noteRepository.save(note)).thenReturn(note);

        // Act
        Note savedNote = this.noteServiceImpl.save(note);

        // Assert
        assertThat(savedNote)
                .isNotNull()
                .isEqualTo(note);
    }

    /**
     * Tests the {@link NoteServiceImpl#existsById(UUID id)} method to ensure it
     * correctly verifies if a note exists in the database by the provided id.
     */
    @Test
    @Order(2)
    @DisplayName("NoteServiceImpl existsById method should return true when note exists")
    public void existsById_ShouldReturnTrue_WhenNoteExists() {
        // Arrange
        UUID id = UUID.fromString("cb4a46cc-1d5a-49ac-a1e6-376f1bbcc338");
        Mockito.when(this.noteRepository.existsById(id)).thenReturn(true);

        // Act
        boolean noteExists = this.noteServiceImpl.existsById(id);

        // Assert
        assertThat(noteExists).isTrue();
    }

    /**
     * Tests the {@link NoteServiceImpl#findById(UUID id)} method to ensure it
     * correctly retrieves a note by the provided id from the database.
     */
    @Test
    @Order(3)
    @DisplayName("NoteServiceImpl findById method should return correct note")
    public void findById_ShouldReturnCorrectNote() {
        // Arrange
        Note note = getNewNote();
        UUID id = UUID.fromString("cb4a46cc-1d5a-49ac-a1e6-376f1bbcc338");

        Mockito.when(this.noteRepository.findById(id)).thenReturn(Optional.of(note));

        // Act
        Optional<Note> foundNote = this.noteServiceImpl.findById(id);

        // Assert
        assertThat(foundNote).isPresent();
        assertThat(foundNote.get()).isEqualTo(note);
    }

    /**
     * Tests the {@link NoteServiceImpl#findAll()} method to ensure it correctly
     * retrieves all notes from the database.
     */
    @Test
    @Order(4)
    @DisplayName("NoteServiceImpl findAll method should return all notes")
    public void findAll_ShouldReturnAllNotes() {
        // Arrange
        List<Note> notes = List.of(getNewNote(), getNewNote());
        Mockito.when(this.noteRepository.findAll()).thenReturn(notes);

        // Act
        List<Note> foundNotes = this.noteServiceImpl.findAll();

        // Assert
        assertThat(foundNotes)
                .hasSize(2)
                .containsAll(notes);
    }

    /**
     * Tests the
     * {@link NoteServiceImpl#findByTitleOrContentContainingIgnoreCase(String term)}
     * method to ensure it finds notes whose title or content contains the specified
     * term, ignoring case.
     */
    @Test
    @Order(5)
    @DisplayName("NoteServiceImpl findByTitleOrContentContainingIgnoreCase method should return correct note")
    public void findByTitleOrContentContainingIgnoreCase_ShouldReturnCorrectNote() {
        // Arrange
        List<Note> notes = List.of(getNewNote(), getNewNote());
        String term = "Content";

        Mockito.when(this.noteRepository.findByTitleOrContentContainingIgnoreCase(term)).thenReturn(notes);

        // Act
        List<Note> foundNotes = this.noteServiceImpl.findByTitleOrContentContainingIgnoreCase(term);

        // Assert
        assertThat(foundNotes)
                .hasSize(2)
                .containsAll(notes);
    }

    /**
     * Tests the {@link NoteServiceImpl#findByTagsInIgnoreCase(List<String> tags)}
     * method to ensure it finds notes whose tags contains at least one of the
     * specified tags, ignoring case.
     */
    @Test
    @Order(6)
    @DisplayName("NoteServiceImpl findByTagsInIgnoreCase method should return correct notes")
    public void findByTagsInIgnoreCase_ShouldReturnCorrectNotes() {
        // Arrange
        List<Note> notes = List.of(getNewNote(), getNewNote());
        Mockito.when(this.noteRepository.findByTagsInIgnoreCase(Mockito.anyList())).thenReturn(notes);

        // Act
        List<Note> foundNotesByTag = this.noteServiceImpl.findByTagsInIgnoreCase(List.of("tag"));
        List<Note> foundNotesByTags = this.noteServiceImpl.findByTagsInIgnoreCase(List.of("Tag", "Another tag"));

        // Assert
        assertThat(foundNotesByTag)
                .hasSize(2)
                .containsAll(notes);

        assertThat(foundNotesByTags)
                .hasSize(2)
                .containsAll(notes);
    }

    /**
     * Tests the {@link NoteServiceImpl#deleteById(UUID id)} service method to
     * ensure it correctly deletes a note correctly by the provided id from the
     * database.
     */
    @Test
    @Order(7)
    @DisplayName("NoteServiceImpl deleteById method should delete correct note")
    public void deleteById_ShouldDeleteCorrectNote() {
        // Arrange
        UUID id = UUID.fromString("cb4a46cc-1d5a-49ac-a1e6-376f1bbcc338");

        // Act
        this.noteServiceImpl.deleteById(id);

        // Assert
        Mockito.verify(this.noteRepository).deleteById(id);
    }

    /**
     * Creates a generic {@link Note} instance for use in tests.
     * 
     * @return The created {@link Note} instance.
     */
    private Note getNewNote() {
        return Note.builder()
                .tags(List.of("Tag", "Another Tag"))
                .title("Untitled")
                .content("Sample content")
                .build();
    }

}