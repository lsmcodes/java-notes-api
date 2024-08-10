package io.github.lsmcodes.notes_api.util.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Handles BCrypt password encryption.
 */
public class BCryptUtil {
    
    private BCryptUtil() {}

    /**
     * Generates a BCrypt hash for the given password.
     * 
     * @param password The plain test password to be hashed.
     * @return The BCrypt hash of the given password as a {@link String}.
     */
    public static String getHash(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        return bCryptPasswordEncoder.encode(password);
    }

}