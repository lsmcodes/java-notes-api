package io.github.lsmcodes.notes_api.service.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.enumeration.UserRole;
import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.user.UserService;

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
    private UserService UserService;

    /**
     * Tests the {@link SecurityService#loadUserByUsername(String username)} to
     * ensure it returns the correct user details based in the provided username.
     */
    @Test
    @DisplayName("SecurityService loadUserByUsername method should return the correct user details")
    public void loadUserByUsername_ShouldLoadTheCorrectUserDetails() {
        // Arrange
        User user = User.builder()
                .name("Default User")
                .username("default_user")
                .password("user1234")
                .role(UserRole.ROLE_USER).build();

        this.UserService.save(user);

        // Act
        UserDetails userDetails = this.securityService.loadUserByUsername("default_user");

        // Assert
        assertThat(userDetails).isEqualTo(user);
    }

}