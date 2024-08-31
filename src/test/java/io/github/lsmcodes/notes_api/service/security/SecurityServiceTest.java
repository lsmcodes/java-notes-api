package io.github.lsmcodes.notes_api.service.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.user.UserService;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

/**
 * Integration tests for the methods provided by the {@link SecurityService}
 * interface.
 */
@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class SecurityServiceTest {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    /**
     * Tests the {@link SecurityService#loadUserByUsername(String username)} to
     * ensure it returns the correct user based in the provided username.
     */
    @Test
    @Order(1)
    @DisplayName("SecurityService loadUserByUsername method should return the correct user details")
    public void loadUserByUsername_ShouldLoadTheCorrectUserDetails() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        user = this.userService.save(user);

        // Act
        UserDetails userDetails = this.securityService.loadUserByUsername(user.getUsername());

        // Assert
        assertThat(userDetails).isEqualTo(user);
    }

    /**
     * Tests the {@link SecurityService#getCurrentAuthenticatedUser()} to
     * ensure it returns the username of the current authenticated user.
     */
    @Test
    @Order(2)
    @DisplayName("SecurityService getCurrentAuthenticatedUser method should return username of the authenticated user")
    public void getCurrentAuthenticatedUser_ShouldReturnUsernameOfTheAuthenticatedUser() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));

        // Act
        String username = this.securityService.getCurrentAuthenticatedUser();

        // Assert
        assertThat(username).isEqualTo(user.getUsername());
    }

}