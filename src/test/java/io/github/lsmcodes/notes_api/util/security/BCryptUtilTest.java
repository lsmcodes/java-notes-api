package io.github.lsmcodes.notes_api.util.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

/**
 * Unit tests for the {@link BCryptUtil} class.
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class BCryptUtilTest {

    /**
     * Tests the {@link BCryptUtil#getHash(String password)} method to ensure that it correctly
     * generates a BCrypt hash for a given password.
     */
    @Test
    @DisplayName("BCryptUtil getHash method should encrypt the password correctly")
    public void getHash_ShouldEncryptThePasswordCorrectly() {
        // Arrange
        String password = "1234567890";

        // Act
        String encryptedPassword = BCryptUtil.getHash(password);

        // Assert
        assertThat(encryptedPassword)
                .isNotEqualTo(password)
                .startsWith("$2a$")
                .hasSize(60);
    }

}