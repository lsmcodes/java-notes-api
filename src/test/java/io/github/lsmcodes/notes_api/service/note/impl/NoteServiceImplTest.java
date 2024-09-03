package io.github.lsmcodes.notes_api.service.note.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.repository.note.NoteRepository;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

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
     * interacts correctly with the {@link NoteRepository#save(Note note)} method
     * providing the specified user.
     */
    @Test
    @Order(1)
    @DisplayName("NoteServiceImpl save method should interact correctly with the repository")
    public void save_ShouldSaveNote() {
        // Arrange
        Note note = NotesApiUtil.getNewNote();
        Mockito.when(this.noteRepository.save(note)).thenReturn(note);

        // Act
        Note savedNote = this.noteServiceImpl.save(note);

        // Assert
        assertThat(savedNote).isNotNull().isEqualTo(note);
    }

    /**
     * Tests the {@link NoteServiceImpl#existsByUserAndId(User user, UUID id)}
     * method to ensure it interacts correctly with the
     * {@link NoteRepository#existsByUserAndId(User user, UUID id)} method providing
     * the specified user and id.
     */
    @Test
    @Order(2)
    @DisplayName("NoteServiceImpl existsByUserAndId method should interact correctly with the repository")
    public void existsByUserAndId_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        UUID id = UUID.randomUUID();

        Mockito.when(this.noteRepository.existsByUserAndId(user, id)).thenReturn(true);

        // Act
        boolean noteExists = this.noteServiceImpl.existsByUserAndId(user, id);

        // Assert
        assertThat(noteExists).isTrue();
    }

    /**
     * Tests the {@link NoteServiceImpl#findByUserAndId(User user, UUID id)} method
     * to ensure it interacts correctly with the
     * {@link NoteRepository#findByUserAndId(User user, UUID id)} method providing
     * the specified user and id.
     */
    @Test
    @Order(3)
    @DisplayName("NoteServiceImpl findByUserAndId method should interact correctly with the repository")
    public void findByUserAndId_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        Note note = NotesApiUtil.getNewNote();
        UUID id = note.getId();

        Mockito.when(this.noteRepository.findByUserAndId(user, id)).thenReturn(Optional.of(note));

        // Act
        Optional<Note> foundNote = this.noteServiceImpl.findByUserAndId(user, id);

        // Assert
        assertThat(foundNote).isPresent();
        assertThat(foundNote.get()).isEqualTo(note);
    }

    /**
     * Tests the {@link NoteServiceImpl#findByUser(User user, Pageable pageable)}
     * method to ensure it interacts correctly with the
     * {@link NoteRepository#findByUser(User user, Pageable pageable)} method
     * providing the specified user and pageable.
     */
    @Test
    @Order(4)
    @DisplayName("NoteServiceImpl findByUser method should interact correctly with the repository")
    public void findByUser_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").descending());

        User user = NotesApiUtil.getNewUser();
        Note firstNote = NotesApiUtil.getNewNote();
        Note secondNote = NotesApiUtil.getNewNote();
        secondNote.setTitle("A sample title");

        Page<Note> page = new PageImpl<>(Arrays.asList(firstNote, secondNote), pageable, 2);
        Mockito.when(this.noteRepository.findByUser(user, pageable)).thenReturn(page);

        // Act
        Page<Note> foundPage = this.noteServiceImpl.findByUser(user, pageable);
        List<Note> pageContent = foundPage.getContent();

        // Assert
        assertThat(foundPage).hasSize(2).isEqualTo(page);
        assertThat(pageContent.get(0)).isEqualTo(firstNote);
        assertThat(pageContent.get(1)).isEqualTo(secondNote);
    }

    /**
     * Tests the
     * {@link NoteServiceImpl#findByUserAndTitleOrContentContainingIgnoreCase(User user, String term, Pageable pageable)}
     * method to ensure it interacts correctly with the
     * {@link NoteRepository#findByUserAndTitleOrContentContainingIgnoreCase(User user, String term, Pageable pageable)}
     * method providing the specified user, term and pageable.
     */
    @Test
    @Order(5)
    @DisplayName("NoteServiceImpl findByUserAndTitleOrContentContainingIgnoreCase method should interact correctly with the repository")
    public void findByUserAndTitleOrContentContainingIgnoreCase_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());

        User user = NotesApiUtil.getNewUser();
        Note firstNote = NotesApiUtil.getNewNote();
        Note secondNote = NotesApiUtil.getNewNote();
        firstNote.setTitle("A sample title");

        Page<Note> page = new PageImpl<>(Arrays.asList(firstNote, secondNote), pageable, 2);
        String term = "Sample";
        Mockito.when(this.noteRepository.findByUserAndTitleOrContentContainingIgnoreCase(user, term, pageable))
                .thenReturn(page);

        // Act
        Page<Note> foundPage = this.noteServiceImpl.findByUserAndTitleOrContentContainingIgnoreCase(user, term,
                pageable);
        List<Note> pageContent = foundPage.getContent();

        // Assert
        assertThat(foundPage).hasSize(2).isEqualTo(page);
        assertThat(pageContent.get(0)).isEqualTo(firstNote);
        assertThat(pageContent.get(1)).isEqualTo(secondNote);
    }

    /**
     * Tests the
     * {@link NoteServiceImpl#findByUserAndTagsInIgnoreCase(User user, List tags, Pageable pageable)}
     * method to ensure it interacts correctly with the
     * {@link NoteRepository#findByUserAndTagsInIgnoreCase(User user, List tags, Pageable pageable)}
     * method providing the specified user, tags and pageable.
     */
    @Test
    @Order(6)
    @DisplayName("NoteServiceImpl findByUserAndTagsInIgnoreCase method should interact correctly with the repository")
    public void findByUserAndTagsInIgnoreCase_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").descending());

        User user = NotesApiUtil.getNewUser();
        Note firstNote = NotesApiUtil.getNewNote();
        Note secondNote = NotesApiUtil.getNewNote();
        secondNote.setTitle("A sample title");

        Page<Note> page = new PageImpl<>(Arrays.asList(firstNote, secondNote), pageable, 2);
        List<String> tags = List.of("tag");
        Mockito.when(this.noteRepository.findByUserAndTagsInIgnoreCase(user, tags, pageable)).thenReturn(page);

        // Act
        Page<Note> foundPage = this.noteServiceImpl.findByUserAndTagsInIgnoreCase(user, tags, pageable);
        List<Note> pageContent = foundPage.getContent();

        // Assert
        assertThat(foundPage).hasSize(2).isEqualTo(page);
        assertThat(pageContent.get(0)).isEqualTo(firstNote);
        assertThat(pageContent.get(1)).isEqualTo(secondNote);
    }

    /**
     * Tests the {@link NoteServiceImpl#deleteByUserAndId(User user, UUID id)}
     * service method to ensure it interacts correctly with the
     * {@link NoteRepository#deleteByUserAndId(User user, UUID id)} method providing
     * the specified user and id.
     */
    @Test
    @Order(7)
    @DisplayName("NoteServiceImpl deleteByUserAndId method should interact correctly with the repository")
    public void deleteByUserAndId_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        UUID id = UUID.randomUUID();

        // Act
        this.noteServiceImpl.deleteByUserAndId(user, id);

        // Assert
        Mockito.verify(this.noteRepository).deleteByUserAndId(user, id);
    }

    /**
     * Tests the {@link NoteServiceImpl#deleteByUser(User user)} service method to
     * ensure it interacts correctly with the
     * {@link NoteRepository#deleteByUser(User user)} method providing the specified
     * user.
     */
    @Test
    @Order(8)
    @DisplayName("NoteServiceImpl deleteByUser method should interact correctly with the repository")
    public void deleteByUser_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        User user = NotesApiUtil.getNewUser();

        // Act
        this.noteServiceImpl.deleteByUser(user);

        // Assert
        Mockito.verify(this.noteRepository).deleteByUser(user);
    }

}