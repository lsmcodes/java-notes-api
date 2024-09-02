package io.github.lsmcodes.notes_api.controller.note;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.lsmcodes.notes_api.dto.model.note.NoteRequestDTO;
import io.github.lsmcodes.notes_api.exception.UserNotFoundException;
import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.note.NoteService;
import io.github.lsmcodes.notes_api.service.user.UserService;
import io.github.lsmcodes.notes_api.service.verification.VerificationService;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

/**
 * Integration tests for the {@link NoteController} class.
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private NoteService noteService;

    @MockBean
    private VerificationService verificationService;

    /**
     * Tests the
     * {@link NoteController#createNote(NoteRequestDTO dto, BindingResult result)}
     * to ensure it correctly creates a new note.
     * 
     * @throws Exception if an error occurs while creating a note.
     */
    @Test
    @Order(1)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("NoteController createNote should create a new note")
    public void createNote_ShouldCreateANewNote() throws Exception {
        // Arrange
        this.setUpAuthenticatedUser();
        NoteRequestDTO noteRequestDTO = new NoteRequestDTO(List.of("Tag"), "Sample Title", "Sample content.");
        Note note = noteRequestDTO.DTOToEntity();

        Mockito.when(this.noteService.save(note)).thenReturn(note);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/notes-api/notes")
                .content(new ObjectMapper().writeValueAsString(noteRequestDTO)).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.tags").value(note.getTags()))
                .andExpect(jsonPath("$.data.title").value(note.getTitle()))
                .andExpect(jsonPath("$.data.content").value(note.getContent()));
    }

    /**
     * Tests the
     * {@link NoteController#findById(UUID id)}
     * to ensure it retrieves the correct note.
     * 
     * @throws Exception if an error occurs while searching for the note.
     */
    @Test
    @Order(2)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("NoteController findById should retrieve the correct note")
    public void findById_ShouldRetrieveANoteWithTheProvidedId() throws Exception {
        // Arrange
        User user = this.setUpAuthenticatedUser();
        Note note = NotesApiUtil.getNewNote();
        UUID id = note.getId();

        Mockito.doNothing().when(this.verificationService).verifyIfNoteExistsByUserAndId(user, id);
        Mockito.when(this.noteService.findByUserAndId(user, id)).thenReturn(Optional.of(note));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/notes-api/notes/{id}", note.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tags[0]").value("Tag"))
                .andExpect(jsonPath("$.data.title").value(note.getTitle()))
                .andExpect(jsonPath("$.data.content").value(note.getContent()));
    }

    /**
     * Tests the
     * {@link NoteController#findAll(int page, int size, String property, String sortDirection)}
     * to ensure it retrieves all saved notes.
     * 
     * @throws Exception if an error occurs while searching for the notes.
     */
    @Test
    @Order(3)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("NoteController findAll should retrieve all saved notes")
    public void findAll_ShouldRetrieveAllNotes() throws Exception {
        // Arrange
        User user = this.setUpAuthenticatedUser();
        Note note = NotesApiUtil.getNewNote();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "title"));
        Page<Note> page = new PageImpl<>(List.of(note), pageable, 1);

        Mockito.when(this.noteService.findByUser(user, pageable)).thenReturn(page);
        Mockito.doNothing().when(this.verificationService).verifyIfPageOfNotesIsNotEmpty(page);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/notes-api/notes")
                .param("page", "0")
                .param("size", "10")
                .param("property", "title")
                .param("sortDirection", "asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].tags[0]").value("Tag"))
                .andExpect(jsonPath("$.data.content[0].title").value(note.getTitle()))
                .andExpect(jsonPath("$.data.content[0].content").value(note.getContent()));
    }

    /**
     * Tests the
     * {@link NoteController#findByTitleOrContentContainingTerm(String term, int page, int size, String property, String sortDirection)}
     * to ensure it retrieves all notes containing the specified term whether in
     * title or content.
     * 
     * @throws Exception if an error occurs while searching for the notes.
     */
    @Test
    @Order(4)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("NoteController findByTitleOrContentContainingTerm should retrieve all notes containing the specified term")
    public void findByTitleOrContentContainingTerm_ShouldRetrieveAllNotesContainingATerm() throws Exception {
        // Arrange
        User user = this.setUpAuthenticatedUser();
        Note note = NotesApiUtil.getNewNote();
        String term = "sample";

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "title"));
        Page<Note> page = new PageImpl<>(List.of(note), pageable, 1);

        Mockito.when(this.noteService.findByUserAndTitleOrContentContainingIgnoreCase(user, term, pageable))
                .thenReturn(page);
        Mockito.doNothing().when(this.verificationService).verifyIfPageOfNotesIsNotEmpty(page);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/notes-api/notes/search/by-term")
                .param("term", term)
                .param("page", "0")
                .param("size", "10")
                .param("property", "title")
                .param("sortDirection", "asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].tags[0]").value("Tag"))
                .andExpect(jsonPath("$.data.content[0].title").value(note.getTitle()))
                .andExpect(jsonPath("$.data.content[0].content").value(note.getContent()));
    }

    /**
     * Tests the
     * {@link NoteController#findByTags(List tags, int page, int size, String property, String sortDirection)}
     * to ensure it retrieves all notes containing at least one of the specified
     * tags.
     * 
     * @throws Exception if an error occurs while searching for the notes.
     */
    @Test
    @Order(5)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("NoteController findByTags should retrieve all notes containing at least one of the specified tags")
    public void findByTags_ShouldRetrieveAllNotesContainingAtLeastOneOfTheSpecifiedTags() throws Exception {
        // Arrange
        User user = this.setUpAuthenticatedUser();
        Note note = NotesApiUtil.getNewNote();
        List<String> tags = note.getTags();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "title"));
        Page<Note> page = new PageImpl<>(List.of(note), pageable, 1);

        Mockito.when(this.noteService.findByUserAndTagsInIgnoreCase(user, tags, pageable)).thenReturn(page);
        Mockito.doNothing().when(this.verificationService).verifyIfPageOfNotesIsNotEmpty(page);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/notes-api/notes/search/by-tags")
                .param("tags", "Tag")
                .param("page", "0")
                .param("size", "10")
                .param("property", "title")
                .param("sortDirection", "asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].tags[0]").value("Tag"))
                .andExpect(jsonPath("$.data.content[0].title").value(note.getTitle()))
                .andExpect(jsonPath("$.data.content[0].content").value(note.getContent()));
    }

    /**
     * Tests the
     * {@link NoteController#updateById(UUID id, NoteRequestDTO dto, BindingResult result)}
     * to ensure it correctly updates and returns the specified note.
     * 
     * @throws Exception if an error occurs while updating the note.
     */
    @Test
    @Order(5)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("NoteController updateById should update and return note")
    public void updateById_ShouldUpdateAndReturnNote() throws Exception {
        // Arrange
        User user = this.setUpAuthenticatedUser();
        Note note = NotesApiUtil.getNewNote();
        UUID id = note.getId();

        Mockito.doNothing().when(this.verificationService).verifyIfNoteExistsByUserAndId(user, id);
        Mockito.when(this.noteService.findByUserAndId(user, id)).thenReturn(Optional.of(note));

        NoteRequestDTO noteRequestDTO = new NoteRequestDTO(List.of("Updated tag"), "Updated Title", "Updated content.");

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/notes-api/notes/{id}", id)
                .content(new ObjectMapper().writeValueAsString(noteRequestDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tags[0]").value("Updated tag"))
                .andExpect(jsonPath("$.data.title").value(noteRequestDTO.getTitle()))
                .andExpect(jsonPath("$.data.content").value(noteRequestDTO.getContent()));
    }

    /**
     * Tests the
     * {@link NoteController#deleteById(UUID id)}
     * to ensure it correctly deletes the specified note and returns a message.
     * 
     * @throws Exception if an error occurs while deleting the note.
     */
    @Test
    @Order(6)
    @WithMockUser(username = "default_user", roles = "USER")
    @DisplayName("NoteController deleteById should delete note and return message")
    public void deleteById_ShouldDeleteNoteAndReturnMessage() throws Exception {
        // Arrange
        User user = this.setUpAuthenticatedUser();
        Note note = NotesApiUtil.getNewNote();
        UUID id = note.getId();

        Mockito.doNothing().when(this.verificationService).verifyIfNoteExistsByUserAndId(user, id);
        Mockito.when(this.noteService.findByUserAndId(user, id)).thenReturn(Optional.of(note));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/notes-api/notes/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("The note was deleted successfully"));
    }

    /**
     * Configures a mock {@link User} entity for use in tests as an authenticated user.
     * 
     * @return The created {@link User} entity.
     * @throws UserNotFoundException if no user was found with the provided username.
     */
    private User setUpAuthenticatedUser() throws UserNotFoundException {
        User user = NotesApiUtil.getNewUser();
        Mockito.doNothing().when(this.verificationService).verifyIfUserExistsByUsername(user.getUsername());
        Mockito.when(this.userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        return user;
    }

}