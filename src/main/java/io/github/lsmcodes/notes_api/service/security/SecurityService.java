package io.github.lsmcodes.notes_api.service.security;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Provides methods for user authentication.
 */
public interface SecurityService extends UserDetailsService {

    /**
     * Returns the username of the current authenticated user.
     * 
     * @return The username of the current user.
     */
    public String getCurrentAuthenticatedUser();

}