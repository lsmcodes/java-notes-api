package io.github.lsmcodes.notes_api.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

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

import io.github.lsmcodes.notes_api.enumeration.UserRole;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

/**
 * Integration tests for the {@link UserRepository} interface.
 */
@ActiveProfiles("test")
@DataJpaTest
@TestMethodOrder(OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * Tests the save repository method to ensure it correctly saves an user to the
     * database.
     */
    @Test
    @Order(1)
    @DisplayName("UserRepository save method should save user")
    public void save_ShouldSaveUser() {
        // Arrange
        User user = User.builder().name("User").username("user").password("user1234").role(UserRole.ROLE_USER).build();

        // Act
        User savedUser = this.userRepository.save(user);

        // Assert
        assertThat(savedUser).isNotNull().isEqualTo(user);
    }

    /**
     * Tests the existsByUsername repository method to ensure it correctly verifies
     * if an user exists in the database by the provided username.
     */
    @Test
    @Order(2)
    @DisplayName("UserRepository existsByUsername method should return true when user exists")
    public void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        // Arrange
        NotesApiUtil.getNewUser(this.userRepository);

        // Act
        boolean userExists = this.userRepository.existsByUsername("default_user");

        // Assert
        assertThat(userExists).isTrue();
    }

    /**
     * Tests the findById repository method to ensure it retrieves correctly an user
     * by the provided id from the database.
     */
    @Test
    @Order(3)
    @DisplayName("UserRepository findById method should return correct user")
    public void findById_ShouldReturnCorrectUser() {
        // Arrange
        User user = NotesApiUtil.getNewUser(this.userRepository);

        // Act
        Optional<User> foundUser = this.userRepository.findById(user.getId());

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    /**
     * Tests the {@link UserRepository#findByUsername(String username)}
     * method to ensure it retrieves correctly an user by the provided username from
     * the database.
     */
    @Test
    @Order(4)
    @DisplayName("UserRepository findByUsername method should return correct user")
    public void findByUsername_ShouldReturnCorrectUser() {
        // Arrange
        User user = NotesApiUtil.getNewUser(this.userRepository);

        // Act
        Optional<User> foundUser = this.userRepository.findByUsername("default_user");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get()).isEqualTo(user);
    }

    /**
     * Tests the deleteById repository method to ensure it correctly deletes an user
     * correctly by the provided id from the database.
     */
    @Test
    @Order(5)
    @DisplayName("UserRepository deleteById method should delete correct user")
    public void deleteById_ShouldDeleteCorrectUser() {
        // Arrange
        User user = NotesApiUtil.getNewUser(this.userRepository);
        UUID userId = user.getId();

        // Act and Assert
        assertThat(this.userRepository.existsById(userId)).isTrue();
        this.userRepository.deleteById(userId);
        assertThat(this.userRepository.existsById(userId)).isFalse();
    }

}