package io.github.lsmcodes.notes_api.service.security.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.lsmcodes.notes_api.model.user.User;
import io.github.lsmcodes.notes_api.service.security.SecurityService;
import io.github.lsmcodes.notes_api.service.user.UserService;

/**
 * Implements {@link SecurityService} interface methods.
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private UserService userService;

    /**
     * Loads user details based on the provided username.
     * 
     * @param username The username used to retrieve user details.
     * @return A {@link UserDetails} object containing the information of the found
     *         user.
     * @throws UsernameNotFoundException if no user with the provided username is
     *                                   found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userService.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("There is no user with the \"" + username + "\" username.");
        }

        return user.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}