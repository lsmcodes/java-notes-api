package io.github.lsmcodes.notes_api.util;

import java.util.List;
import java.util.UUID;

import org.springframework.validation.BindingResult;

import io.github.lsmcodes.notes_api.enumeration.UserRole;
import io.github.lsmcodes.notes_api.model.note.Note;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.repository.note.NoteRepository;
import io.github.lsmcodes.notes_api.repository.user.UserRepository;

/**
 * Provides utility methods.
 */
public class NotesApiUtil {

    /**
     * Returns all error messages contained contained in a provided
     * {@link BindingResult}.
     * 
     * @param result The {@link BindingResult} containing the result of the
     *               validation checks on a DTO.
     * @return A {@link List} with all error messages contained in the provided
     *         {@link BindingResult}.
     */
    public static List<String> getResponseErrorMessages(BindingResult result) {
        return result.getAllErrors().stream().map(error -> error.getDefaultMessage()).toList();
    }

    /**
     * Creates and saves a generic {@link Note} instance for use in tests.
     * 
     * @param repository The repository where the Note instance will be saved.
     * @return The created {@link Note} instance.
     */
    public static Note getNewNote(NoteRepository repository) {
        Note note = Note.builder().tags(List.of("Tag")).title("Sample Title").content("Sample content.").build();
        return repository.save(note);
    }

    /**
     * Creates a generic {@link Note} instance for use in tests.
     * 
     * @return The created {@link Note} instance.
     */
    public static Note getNewNote() {
        return Note.builder().id(UUID.randomUUID()).tags(List.of("Tag")).title("Sample Title")
                .content("Sample content.").build();
    }

    /**
     * Creates and saves a generic {@link User} instance for use in tests.
     * 
     * @param repository The repository where the User instance will be saved.
     * @return The created {@link User} instance.
     */
    public static User getNewUser(UserRepository repository) {
        User user = User.builder().name("Default User").username("default_user").password("1234567890")
                .role(UserRole.ROLE_USER).build();
        return repository.save(user);
    }

    /**
     * Creates a generic {@link User} instance for use in tests.
     * 
     * @return The created {@link User} instance.
     */
    public static User getNewUser() {
        return User.builder().id(UUID.randomUUID()).name("Default User").username("default_user").password("1234567890")
                .role(UserRole.ROLE_USER).build();
    }

}