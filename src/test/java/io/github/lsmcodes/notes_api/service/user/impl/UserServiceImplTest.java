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

import io.github.lsmcodes.notes_api.enumeration.UserRole;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.repository.user.UserRepository;

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
     * correctly saves an user to the database.
     */
    @Order(1)
    @DisplayName("UserServiceImpl save method should save user")
    @Test
    public void save_ShouldSaveUser() {
        // Arrange
        User user = getNewUser();
        Mockito.when(this.userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = this.userServiceImpl.save(user);

        // Assert
        assertThat(savedUser)
                .isNotNull()
                .isEqualTo(user);
    }

    /**
     * Tests the {@link UserServiceImpl#existsByUsername(String username)} method to
     * ensure it correctly verifies if an user exists in the database by the
     * provided username.
     */
    @Test
    @Order(2)
    @DisplayName("UserServiceImpl existsByUsername method should return true when user exists")
    public void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        // Arrange
        Mockito.when(this.userRepository.existsByUsername("default_user")).thenReturn(true);

        // Act
        boolean userExists = this.userServiceImpl.existsByUsername("default_user");

        // Assert
        assertThat(userExists).isTrue();
    }

    /**
     * Tests the {@link UserServiceImpl#findById(UUID id)} method to ensure it
     * correctly retrieves an user by the provided id from the database.
     */
    @Test
    @Order(3)
    @DisplayName("UserServiceImpl findById method should return correct user")
    public void findById_ShouldReturnCorrectUser() {
        // Arrange
        User user = getNewUser();
        UUID id = UUID.fromString("8b94e336-4c1d-4014-8b2c-70300e443e97");

        Mockito.when(this.userRepository.findById(id)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = this.userServiceImpl.findById(id);

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    /**
     * Tests the {@link UserServiceImpl#findByUsername(String username)} method to
     * ensure it correctly retrieves an user by the provided username from the
     * database.
     */
    @Test
    @Order(4)
    @DisplayName("UserServiceImpl findByUsername method should return correct user")
    public void findByUsername_ShouldReturnCorrectUser() {
        // Arrange
        User user = getNewUser();
        String username = "default_user";

        Mockito.when(this.userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = this.userServiceImpl.findByUsername(username);

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    /**
     * Tests the {@link UserServiceImpl#deleteById(UUID id)} method to
     * ensure it correctly deletes an user correctly by the provided id from the
     * database.
     */
    @Test
    @Order(5)
    @DisplayName("UserServiceImpl deleteById method should delete correct user")
    public void deleteById_ShouldDeleteCorrectUser() {
        // Arrange
        UUID id = UUID.fromString("8b94e336-4c1d-4014-8b2c-70300e443e97");

        // Act
        this.userServiceImpl.deleteById(id);

        // Assert
        Mockito.verify(this.userRepository).deleteById(id);
    }

    /**
     * Creates a generic {@link User} instance for use in tests.
     * 
     * @return The created {@link User} instance.
     */
    private User getNewUser() {
        return User.builder()
                .name("Default User")
                .username("default_user")
                .password("user1234")
                .role(UserRole.ROLE_USER).build();
    }

}