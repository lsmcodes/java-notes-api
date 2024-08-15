package io.github.lsmcodes.notes_api.service.security;

import java.util.Date;

import io.github.lsmcodes.notes_api.model.user.User;

/**
 * Provides methods for manipulating tokens
 */
public interface TokenService {

    /**
     * Generates a JWT token from a User object.
     * 
     * @param user The user object from which the token will be generated.
     * @return The generated token as a {@link String}.
     */
    public String generateToken(User user);

    /**
     * Extracts the claims from a JWT token.
     * 
     * @param token The JWT token from which the subject will be extracted.
     * @return The extracted subject as a {@link String}.
     */
    public String getSubjectFromToken(String token);

    /**
     * Generates an expiration date based on the current system time and the defined
     * expiration period.
     * 
     * @return The generated expiration date as a {@link Date} object.
     */
    public Date generateExpirationDate();

}