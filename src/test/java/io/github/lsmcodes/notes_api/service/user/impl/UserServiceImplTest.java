package io.github.lsmcodes.notes_api.service.user.impl;

import static org.assertj.core.api.Assertions.assertThat;

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

import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.repository.user.UserRepository;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

/**
 * Unit tests for the {@link UserServiceImpl} class.
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    /**
     * Tests the {@link UserServiceImpl#save(User user)} method to ensure it
     * interacts correctly with the {@link UserRepository#save(User user)} method
     * providing the specified user.
     */
    @Order(1)
    @DisplayName("UserServiceImpl save method should interact correctly with the repository")
    @Test
    public void save_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        Mockito.when(this.userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = this.userServiceImpl.save(user);

        // Assert
        assertThat(savedUser).isNotNull().isEqualTo(user);
    }

    /**
     * Tests the {@link UserServiceImpl#existsByUsername(String username)} method to
     * ensure it interacts correctly with the
     * {@link UserRepository#existsByUsername(String username)} method providing the
     * specified username.
     */
    @Test
    @Order(2)
    @DisplayName("UserServiceImpl existsByUsername method should interact correctly with the repository")
    public void existsByUsername_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        Mockito.when(this.userRepository.existsByUsername("default_user")).thenReturn(true);

        // Act
        boolean userExists = this.userServiceImpl.existsByUsername("default_user");

        // Assert
        assertThat(userExists).isTrue();
    }

    /**
     * Tests the {@link UserServiceImpl#findById(UUID id)} method to ensure it
     * interacts correctly with the {@link UserRepository#findById(UUID id)}
     * method providing the specified id.
     */
    @Test
    @Order(3)
    @DisplayName("UserServiceImpl findById method should interact correctly with the repository")
    public void findById_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        UUID id = user.getId();

        Mockito.when(this.userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = this.userServiceImpl.findById(id);

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    /**
     * Tests the {@link UserServiceImpl#findByUsername(String username)} method to
     * ensure it interacts correctly with the
     * {@link UserRepository#findByUsername(String username)} method providing the
     * specified username.
     */
    @Test
    @Order(4)
    @DisplayName("UserServiceImpl findByUsername method should interact correctly with the repository")
    public void findByUsername_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        String username = user.getUsername();

        Mockito.when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = this.userServiceImpl.findByUsername(username);

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    /**
     * Tests the {@link UserServiceImpl#deleteById(UUID id)} method to ensure it
     * interacts correctly with the {@link UserRepository#deleteById(UUID id)}
     * method providing the specified id.
     */
    @Test
    @Order(5)
    @DisplayName("UserServiceImpl deleteById method should interact correctly with the repository")
    public void deleteById_ShouldInteractCorrectlyWithTheRepository() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        this.userServiceImpl.deleteById(id);

        // Assert
        Mockito.verify(this.userRepository).deleteById(id);
    }

}