package io.github.lsmcodes.notes_api.service.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.util.NotesApiUtil;

/**
 * Integration tests for the methods provided by the {@link TokenService}
 * interface.
 */
@ActiveProfiles("test")
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    /**
     * Tests the {@link TokenService#generateToken(User user)} to ensure it
     * generates a valid token.
     */
    @Test
    @Order(1)
    @DisplayName("TokenService generateToken method should return a valid token")
    public void generateToken_ShouldReturnAValidToken() {
        // Arrange
        User user = NotesApiUtil.getNewUser();

        // Act
        String token = this.tokenService.generateToken(user);
        System.out.println(token);

        // Assert
        assertThat(token).isNotNull().startsWith("ey");
        assertThat(tokenService.getSubjectFromToken(token)).isEqualTo(user.getUsername());
        assertThat(token.split("\\.")).hasSize(3);
    }

    /**
     * Tests the {@link TokenService#getSubjectFromToken(String token)} to ensure it
     * returns the correct subject of the provided token.
     */
    @Test
    @Order(2)
    @DisplayName("TokenService getSubjectFromToken method should return the correct subject")
    public void getSubjectFromToken_ShouldReturnTheCorrectSubject() {
        // Arrange
        User user = NotesApiUtil.getNewUser();
        String token = this.tokenService.generateToken(user);

        // Act
        String subject = this.tokenService.getSubjectFromToken(token);

        // Assert
        assertThat(subject).isEqualTo(user.getUsername());
    }

    /**
     * Tests the {@link TokenService#generateExpirationDate()} to ensure it
     * returns a valid expiration date.
     */
    @Test
    @Order(3)
    @DisplayName("TokenService generateExpirationDate method should return a valid expiration date")
    public void generateExpirationDate_ShouldReturnAValidExpirationDate() {
        // Arrange
        Date currentDate = new Date();
        System.out.println(currentDate);

        // Act
        Date expirationDate = tokenService.generateExpirationDate();
        System.out.println(expirationDate);

        // Assert
        assertThat(expirationDate).isNotEqualTo(currentDate).isAfter(currentDate);
    }

}