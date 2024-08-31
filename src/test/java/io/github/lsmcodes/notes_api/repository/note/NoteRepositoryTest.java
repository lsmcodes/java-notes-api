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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.repository.user.UserRepository;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

/**
 * Integration tests for the {@link NoteRepository} interface.
 */
@ActiveProfiles("test")
@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Tests the save repository method to ensure it correctly saves a note to the
     * database.
     */
    @Test
    @Order(1)
    @DisplayName("NoteRepository save method should save note")
    public void save_ShouldSaveNote() {
        // Arrange
        Note note = Note.builder().tags(List.of("Tag")).title("Sample Title").content("Sample content.").build();

        // Act
        Note savedNote = this.noteRepository.save(note);

        // Assert
        assertThat(savedNote).isNotNull().isEqualTo(note);
    }

    /**
     * Tests the {@link NoteRepository#existsByUserAndId(User user, UUID id)}
     * repository method to ensure it correctly verifies if a note exists in the
     * database by the provided user and id.
     */
    @Test
    @Order(2)
    @DisplayName("NoteRepository existsByUserAndId method should return true when note exists")
    public void existsByUserAndId_ShouldReturnTrue_WhenNoteExists() {
        // Arrange
        User user = NotesApiUtil.getNewUser(this.userRepository);
        Note note = NotesApiUtil.getNewNote(this.noteRepository);

        user.getNotes().add(note);
        note.setUser(user);

        // Act
        boolean noteExists = this.noteRepository.existsByUserAndId(user, note.getId());

        // Assert
        assertThat(noteExists).isTrue();
    }

    /**
     * Tests the {@link NoteRepository#findByUserAndId(User user, UUID id)}
     * repository method to ensure it correctly retrieves a note by the provided
     * user and id from the database.
     */
    @Test
    @Order(3)
    @DisplayName("NoteRepository findByUserAndId method should return correct note")
    public void findByUserAndId_ShouldReturnCorrectNote() {
        // Arrange
        User user = NotesApiUtil.getNewUser(this.userRepository);
        Note note = NotesApiUtil.getNewNote(this.noteRepository);

        user.getNotes().add(note);
        note.setUser(user);

        // Act
        Optional<Note> foundNote = this.noteRepository.findByUserAndId(user, note.getId());

        // Assert
        assertThat(foundNote).isPresent();
        assertThat(foundNote.get()).isEqualTo(note);
    }

    /**
     * Tests the {@link NoteRepository#findByUser(User user, Pageable pageable)}
     * repository method to ensure it correctly retrieves all notes from a user.
     */
    @Test
    @Order(4)
    @DisplayName("NoteRepository findByUser method should return all notes")
    public void findByUser_ShouldReturnAllNotes() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());

        User user = NotesApiUtil.getNewUser(this.userRepository);
        Note firstNote = NotesApiUtil.getNewNote(this.noteRepository);
        Note secondNote = NotesApiUtil.getNewNote(this.noteRepository);
        firstNote.setTitle("A sample title");

        user.getNotes().addAll(List.of(firstNote, secondNote));
        firstNote.setUser(user);
        secondNote.setUser(user);

        // Act
        Page<Note> foundPage = this.noteRepository.findByUser(user, pageable);
        List<Note> pageContent = foundPage.getContent();

        // Assert
        assertThat(pageContent).hasSize(2);
        assertThat(pageContent.get(0)).isEqualTo(firstNote);
        assertThat(pageContent.get(1)).isEqualTo(secondNote);
    }

    /**
     * Tests the
     * {@link NoteRepository#findByUserAndTitleOrContentContainingIgnoreCase(User user, String term, Pageable pageable)}
     * method to ensure it finds notes from the provided user whose title or content
     * contains the specified term, ignoring case.
     */
    @Test
    @Order(5)
    @DisplayName("NoteRepository findByUserAndTitleOrContentContainingIgnoreCase method should return correct note")
    public void findByUserAndTitleOrContentContainingIgnoreCase_ShouldReturnCorrectNote() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").descending());

        User user = NotesApiUtil.getNewUser(this.userRepository);
        Note firstNote = NotesApiUtil.getNewNote(this.noteRepository);
        Note secondNote = NotesApiUtil.getNewNote(this.noteRepository);
        secondNote.setTitle("A sample title");

        user.getNotes().addAll(List.of(firstNote, secondNote));
        firstNote.setUser(user);
        secondNote.setUser(user);

        // Act
        Page<Note> foundPage = this.noteRepository.findByUserAndTitleOrContentContainingIgnoreCase(user, "sample",
                pageable);
        List<Note> pageContent = foundPage.getContent();

        // Assert
        assertThat(pageContent).hasSize(2);
        assertThat(pageContent.get(0)).isEqualTo(firstNote);
        assertThat(pageContent.get(1)).isEqualTo(secondNote);
    }

    /**
     * Tests the
     * {@link NoteRepository#findByUserAndTagsInIgnoreCase(User user, List tags, Pageable pageable)}
     * method to ensure it finds notes from the provided user whose tags contains at
     * least one of the specified tags, ignoring case.
     */
    @Test
    @Order(6)
    @DisplayName("NoteRepository findByUserAndTagsInIgnoreCase method should return correct notes")
    public void findByUserAndTagsInIgnoreCase_ShouldReturnCorrectNotes() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());

        User user = NotesApiUtil.getNewUser(this.userRepository);
        Note firstNote = NotesApiUtil.getNewNote(this.noteRepository);
        Note secondNote = NotesApiUtil.getNewNote(this.noteRepository);
        firstNote.setTitle("A sample title");

        user.getNotes().addAll(List.of(firstNote, secondNote));
        firstNote.setUser(user);
        secondNote.setUser(user);

        // Act
        Page<Note> foundPage = this.noteRepository.findByUserAndTagsInIgnoreCase(user, List.of("tag"), pageable);
        List<Note> pageContent = foundPage.getContent();

        // Assert
        assertThat(pageContent).hasSize(2);
        assertThat(pageContent.get(0)).isEqualTo(firstNote);
        assertThat(pageContent.get(1)).isEqualTo(secondNote);
    }

    /**
     * Tests the {@link NoteRepository#deleteByUserAndId(User user, UUID id)}
     * repository method to ensure it correctly deletes a note correctly by the
     * provided user and id from the database.
     */
    @Test
    @Order(7)
    @DisplayName("NoteRepository deleteByUserAndId method should delete correct note")
    public void deleteByUserAndId_ShouldDeleteCorrectNote() {
        // Arrange
        User user = NotesApiUtil.getNewUser(this.userRepository);
        Note note = NotesApiUtil.getNewNote(this.noteRepository);
        UUID noteId = note.getId();

        user.getNotes().add(note);
        note.setUser(user);

        // Act and Assert
        assertThat(this.noteRepository.existsByUserAndId(user, noteId)).isTrue();
        user.getNotes().remove(note);
        this.noteRepository.deleteByUserAndId(user, noteId);
        assertThat(this.noteRepository.existsByUserAndId(user, noteId)).isFalse();
    }

}