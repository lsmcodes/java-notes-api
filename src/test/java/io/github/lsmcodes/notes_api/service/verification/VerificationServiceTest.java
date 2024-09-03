package io.github.lsmcodes.notes_api.service.verification;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.exception.NoteNotFoundException;
import io.github.lsmcodes.notes_api.exception.UserNotFoundException;
import io.github.lsmcodes.notes_api.exception.UsernameAlreadyExistsException;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.repository.user.UserRepository;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

/**
 * Integration tests for the {@link VerificationService} interface.
 */
@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class VerificationServiceTest {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void cleanRepository() {
        this.userRepository.deleteAll();
    }

    /**
     * Tests the
     * {@link VerificationService#verifyIfUserExistsByUsername(String username)} to
     * ensure it correctly throws a {@link UserNotFoundException} when user does not
     * exist.
     */
    @Test
    @Order(1)
    @DisplayName("VerificationService verifyIfUserExistsByUsername method should throw UserNotFoundException")
    public void verifyIfUserExistsByUsername_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> {
            this.verificationService.verifyIfUserExistsByUsername("user");
        });
    }

    /**
     * Tests the
     * {@link VerificationService#verifyIfUsernameDoesNotExist(String username)} to
     * ensure it correctly throws a {@link UsernameAlreadyExistsException} when the
     * username exists.
     */
    @Test
    @Order(2)
    @DisplayName("VerificationService verifyIfUsernameDoesNotExist method should throw UsernameAlreadyExistsException")
    public void verifyIfUsernameDoesNotExist_ShouldThrowUsernameAlreadyExistsException_WhenUsernameExists() {
        // Arrange
        NotesApiUtil.getNewUser(this.userRepository);

        // Act and Assert
        assertThrows(UsernameAlreadyExistsException.class,
                () -> this.verificationService.verifyIfUsernameDoesNotExist("default_user"));
    }

    /**
     * Tests the
     * {@link VerificationService#verifyIfNoteExistsByUserAndId(User user, UUID id)}
     * to ensure it correctly throws a {@link NoteNotFoundException} when the note
     * does not exist.
     */
    @Test
    @Order(3)
    @DisplayName("VerificationService verifyIfNoteExistsByUserAndId method should throw NoteNotFoundException")
    public void verifyIfNoteExistsByUserAndId_ShouldThrowNoteNotFoundException_WhenNoteDoesNotExist() {
        // Arrange
        User user = NotesApiUtil.getNewUser(this.userRepository);

        // Act and Assert
        assertThrows(NoteNotFoundException.class,
                () -> this.verificationService.verifyIfNoteExistsByUserAndId(user, UUID.randomUUID()));
    }

    /**
     * Tests the {@link VerificationService#verifyIfCurrentUserIsAuthenticated()} to
     * ensure it correctly returns true when user is authenticated.
     */
    @Test
    @Order(4)
    @DisplayName("VerificationService verifyIfCurrentUserIsAuthenticated method should return true when user is authenticated")
    public void verifyIfCurrentUserIsAuthenticated_ShouldReturnTrue_WhenUserIsAuthenticated() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));

        // Act and Assert
        assertTrue(this.verificationService.verifyIfCurrentUserIsAuthenticated());
        SecurityContextHolder.clearContext();
        assertFalse(this.verificationService.verifyIfCurrentUserIsAuthenticated());
    }

    /**
     * Tests the {@link VerificationService#verifyIfCurrentUserIsNotAuthenticated()}
     * to ensure it correctly returns true when user is not authenticated.
     */
    @Test
    @Order(5)
    @DisplayName("VerificationService verifyIfCurrentUserIsNotAuthenticated method should return true when user is not authenticated")
    public void verifyIfCurrentUserIsNotAuthenticated_ShouldReturnTrue_WhenUserIsNotAuthenticated() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act and Assert
        assertTrue(this.verificationService.verifyIfCurrentUserIsNotAuthenticated());
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new AnonymousAuthenticationToken("unique-key", "anonymousUser",
                                List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));
        assertTrue(this.verificationService.verifyIfCurrentUserIsNotAuthenticated());
    }

}