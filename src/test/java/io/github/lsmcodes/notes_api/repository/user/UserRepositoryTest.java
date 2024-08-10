package io.github.lsmcodes.notes_api.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.enumeration.UserRole;
import io.github.lsmcodes.notes_api.model.user.User;

/**
 * Tests UserRepository methods
 */
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * Tests the repository method for saving users
     */
    @Test
    @Order(1)
    @DisplayName("Save should save user")
    public void save_ShouldSaveNote() {
        // Arrange
        User user = User.builder()
                .name("User")
                .username("user")
                .password("user1234")
                .role(UserRole.ROLE_USER).build();

        // Act
        User savedUser = this.userRepository.save(user);

        // Assert
        assertThat(savedUser)
                .isNotNull()
                .isEqualTo(user);
    }

    /**
     * Tests the repository method for verifying if an user exists by username
     */
    @Test
    @Order(2)
    @DisplayName("Exists by username should return true when user exists")
    public void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        // Arrange
        getNewUser();

        // Act
        boolean userExists = this.userRepository.existsByUsername("default_user");

        // Assert
        assertThat(userExists).isTrue();
    }

    /**
     * Tests the repository method for verifying if an user exists by username
     */
    @Test
    @Order(3)
    @DisplayName("Exists by username should return false when user does not exist")
    public void existsByUsername_ShouldReturnFalse_WhenUserDoesNotExist() {
        // Act
        boolean userExists = this.userRepository.existsByUsername("admin");

        // Assert
        assertThat(userExists).isFalse();
    }

    /**
     * Tests the repository method for finding users by username
     */
    @Test
    @Order(3)
    @DisplayName("Find by username should return correct user")
    public void findByUsername_ShouldReturnCorrectUser() {
        // Arrange
        User user = getNewUser();

        // Act
        User foundUser = this.userRepository.findByUsername("default_user").get();

        // Assert
        assertThat(foundUser).isEqualTo(user);
    }

    /**
     * Creates and saves an User object for use in tests
     * 
     * @return User
     */
    private User getNewUser() {
        User user = User.builder()
                .name("Default User")
                .username("default_user")
                .password("user1234")
                .role(UserRole.ROLE_USER).build();
        return this.userRepository.save(user);
    }

}